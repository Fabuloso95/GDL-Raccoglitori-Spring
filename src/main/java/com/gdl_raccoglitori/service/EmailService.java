package com.gdl_raccoglitori.service;

import java.util.List;

public interface EmailService
{
	public void sendResetPasswordEmail(String email, String token);
	public void sendWelcomeEmail(String email, String nomeUtente);
	public void sendBookSelectionNotification(List<String> recipients, String bookTitle);
	public void sendVotingStartedNotification(List<String> recipients);
	public void sendZoomReminder(List<String> recipients, String topic, String zoomLink);
}
