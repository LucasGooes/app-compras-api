package br.com.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursomc.domain.Cliente;
import br.com.cursomc.domain.ItemPedido;
import br.com.cursomc.domain.PagamentoComBoleto;
import br.com.cursomc.domain.Pedido;
import br.com.cursomc.domain.enums.EstadoPagamento;
import br.com.cursomc.repositories.ItemPedidoRepository;
import br.com.cursomc.repositories.PagamentoRepository;
import br.com.cursomc.repositories.PedidoRepository;
import br.com.cursomc.security.UserSS;
import br.com.cursomc.services.exceptions.AuthorizationException;
import br.com.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	@Autowired
	private PedidoRepository repo;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	@Autowired
	private ProdutoService produtoService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("OBJETO NÃO ENCONTRADO! Id: "+id
					+", Tipo: "+Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user==null) {
			throw new AuthorizationException("Acesso Negado");
		}
		PageRequest pageRequest = PageRequest.of( page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}

}
