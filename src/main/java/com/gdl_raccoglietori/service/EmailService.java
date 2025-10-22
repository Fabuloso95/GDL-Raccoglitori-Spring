package com.gdl_raccoglietori.service;

public interface EmailService
{
	public void sendResetPasswordEmail(String email, String token);
}
