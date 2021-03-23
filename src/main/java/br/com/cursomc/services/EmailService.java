package br.com.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import br.com.cursomc.domain.Cliente;
import br.com.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderComfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
