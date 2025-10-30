package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService
{
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Autowired
	public EmailServiceImpl(JavaMailSender mailSender)
	{
		this.mailSender = mailSender;
	}

	private void sendHtmlEmail(String recipient, String subject, String content)
	{
		try
		{
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setFrom(senderEmail);
			helper.setTo(recipient);
			helper.setSubject(subject);
			helper.setText(content, true);
			
			mailSender.send(message);
		} 
		catch (MessagingException e)
		{
			System.err.println("Errore nell'invio dell'email a " + recipient + ": " + e.getMessage());
		}
	}
	
	private void sendHtmlEmailToMultiple(List<String> recipients, String subject, String content)
	{
		if (recipients == null || recipients.isEmpty())
		{
			return;
		}
		
		String[] toArray = recipients.toArray(new String[0]);
		
		try 
		{
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setFrom(senderEmail);
			helper.setTo(toArray); 
			helper.setSubject(subject);
			helper.setText(content, true);
			
			mailSender.send(message);
		} 
		catch (MessagingException e) 
		{
			System.err.println("Errore nell'invio dell'email multipla: " + e.getMessage());
		}
	}

	@Override
	public void sendResetPasswordEmail(String email, String token)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(senderEmail);
		message.setTo(email);
		message.setSubject("Reset della tua password per GDL Raccoglitori");
		
		String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
		
		String text = String.format(
			"Ciao,\n\n" +
			"Abbiamo ricevuto una richiesta di reset della password per il tuo account.\n" +
			"Clicca sul link qui sotto per procedere:\n\n" +
			"%s\n\n" +
			"Se non hai richiesto il reset, ignora questa email.\n\n" +
			"Il team GDL Raccoglitori",
			resetLink
		);
		message.setText(text);

		mailSender.send(message);
	}

	@Override
	public void sendWelcomeEmail(String email, String nomeUtente)
	{
		String subject = "Benvenuto/a nel GDL Raccoglitori!";
		
		String htmlContent = String.format(
			"<html>" +
				"<body style='font-family: sans-serif; color: #333;'>" +
				"<h2 style='color: #4CAF50;'>Ciao %s, benvenuto/a!</h2>" +
				"<p>Siamo entusiasti di averti nel nostro Club del Libro. Preparati a scoprire nuovi mondi e a condividere le tue passioni letterarie.</p>" +
				"<p><strong>Cosa puoi fare ora?</strong></p>" +
				"<ul>" +
				"<li>Dai un'occhiata al libro del mese corrente.</li>" +
				"<li>Vota per le prossime selezioni.</li>" +
				"<li>Partecipa alla prossima discussione Zoom!</li>" +
				"</ul>" +
				"<p>Buona lettura!</p>" +
				"<p>Il Team GDL Raccoglitori</p>" +
				"</body>" +
			"</html>", 
			nomeUtente
		);
		
		sendHtmlEmail(email, subject, htmlContent);
	}
	
	@Override
	public void sendBookSelectionNotification(List<String> recipients, String bookTitle)
	{
		String subject = "📚 IL LIBRO È STATO SCELTO: Pronti a leggere \"" + bookTitle + "\"!";
		
		String htmlContent = String.format(
			"<html>" +
				"<body style='font-family: sans-serif; color: #333;'>" +
				"<h2 style='color: #FF9800;'>Ecco la nostra prossima avventura!</h2>" +
				"<p>Siamo lieti di annunciare che il libro scelto per la discussione di questo mese è:</p>" +
				"<div style='border: 2px solid #FF9800; padding: 15px; border-radius: 8px; background-color: #FFF3E0; margin: 20px 0;'>" +
				"<h3 style='margin: 0; color: #D84315;'>\"%s\"</h3>" +
				"</div>" +
				"<p>Inizia a leggere! La discussione si terrà tra circa un mese.</p>" +
				"<p>A presto, e buona immersione!</p>" +
				"</body>" +
			"</html>", 
			bookTitle
		);

		sendHtmlEmailToMultiple(recipients, subject, htmlContent);
	}

	@Override
	public void sendVotingStartedNotification(List<String> recipients)
	{
		String subject = "🚨 VOTAZIONE IN CORSO: Scegli il prossimo libro in 24 Ore!";
		
		String htmlContent = 
			"<html>" +
				"<body style='font-family: sans-serif; color: #333;'>" +
				"<h2 style='color: #2196F3;'>Il potere è nelle tue mani!</h2>" +
				"<p>La votazione per la prossima selezione del GDL Raccoglitori è ufficialmente iniziata.</p>" +
				"<p>Hai solo **24 ore** per esprimere la tua preferenza e decidere il libro del mese. Non perdere questa occasione!</p>" +
				"<div style='text-align: center; margin: 30px 0;'>" +
				"<a href='[LINK_VOTAZIONE]' style='background-color: #2196F3; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>VOTA ORA</a>" +
				"</div>" +
				"<p><small>Ricorda di sostituire [LINK_VOTAZIONE] con il link effettivo alla tua pagina di votazione.</small></p>" +
				"</body>" +
			"</html>";
		
		sendHtmlEmailToMultiple(recipients, subject, htmlContent);
	}

	@Override
	public void sendZoomReminder(List<String> recipients, String topic, String zoomLink)
	{
		String subject = "🔔 PROMEMORIA: Discussione Zoom domani su " + topic;
		
		String htmlContent = String.format(
			"<html>" +
				"<body style='font-family: sans-serif; color: #333;'>" +
				"<h2 style='color: #673AB7;'>Manca solo un giorno!</h2>" +
				"<p>Volevi un promemoria amichevole: la nostra discussione sul tema <strong>\"%s\"</strong> è fissata per **domani**!</p>" +
				"<p>Prepara le tue frasi preferite e i tuoi pensieri più caldi. Non vediamo l'ora di sentirti!</p>" +
				"<div style='text-align: center; margin: 30px 0;'>" +
				"<p style='font-size: 1.1em; font-weight: bold;'>Link Zoom:</p>" +
				"<a href='%s' style='background-color: #673AB7; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>UNISCITI ALLA RIUNIONE</a>" +
				"</div>" +
				"<p>A domani!</p>" +
				"</body>" +
			"</html>", 
			topic, zoomLink
		);
		
		sendHtmlEmailToMultiple(recipients, subject, htmlContent);
	}
}
