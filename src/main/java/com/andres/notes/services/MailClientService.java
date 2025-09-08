package com.andres.notes.services;


import com.andres.notes.exceptions.MailServiceException;
import com.andres.notes.util.HmcaUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.TreeMap;

@Service
public class MailClientService {

    @Value("${mail.service.url}")
    private String mailServiceUrl;

    @Value("${mail.secret.key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void sendMail(String to, String subjet, String text) {
        try{
            Map<String, String> bodyMap = new TreeMap<>();
            bodyMap.put("to",to);
            bodyMap.put("subject",subjet);
            bodyMap.put("html",text);

            String bodyJson = objectMapper.writeValueAsString(bodyMap);

            String timestamp = String.valueOf(System.currentTimeMillis());


            // Creando firma
            String signature = HmcaUtil.generateSignature(secretKey, timestamp, bodyJson);


            // Headers
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-timestamp", timestamp);
            headers.set("x-signature", signature);


            HttpEntity<String> entity = new HttpEntity<>(bodyJson, headers);


            // Hacer request
            ResponseEntity<String> response = restTemplate.exchange(
                    mailServiceUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );





            System.out.println("Respuesta del servicio " + response.getBody());


            if(!response.getStatusCode().is2xxSuccessful()){
                throw  new MailServiceException("Error sending email " + response.getStatusCode());
            }
        } catch (JsonProcessingException e) {
            throw new MailServiceException("Fallo al enviar correo "+e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public static String buildActivationTemplate(String name, String code, String activationLink) {
        return String.format("""
    <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="background-color:#f2f4f7;padding:20px 0;font-family:Arial,Helvetica,sans-serif;">
      <tr>
        <td align="center">
          <table width="600" cellpadding="0" cellspacing="0" border="0" style="background:#ffffff;border:1px solid #ddd;">
            <tr>
              <td style="padding:20px;background:#4f46e5;color:#ffffff;text-align:center;">
                <h1 style="margin:0;font-size:18px;">Welcome to MyApp</h1>
                <p style="margin:5px 0 0;font-size:13px;">Activate your account to get started</p>
              </td>
            </tr>
            <tr>
              <td style="padding:20px;color:#333333;font-size:14px;">
                <p style="margin:0 0 15px;">Hello <strong>%s</strong>,</p>
                <p style="margin:0 0 10px;">Your activation code is:</p>
                <p style="margin:0 0 20px;font-size:20px;letter-spacing:4px;font-weight:bold;text-align:center;">
                  %s
                </p>
                <p style="margin:0 0 10px;">activate your account here:</p>
                <p style="text-align:center;margin:0 0 20px;">
                  <a href="%s" style="display:inline-block;padding:10px 18px;background:#4f46e5;color:#ffffff;text-decoration:none;font-weight:bold;">
                    Activate my account
                  </a>
                </p>
                <p style="margin:15px 0 0;font-size:12px;color:#888888;">
                  If you didn’t request this email, just ignore it.
                </p>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    """, name, code, activationLink);
    }


    public static String buildResetPasswordTemplate(String name, String resetLink) {
        return String.format("""
<table width="100%%" cellpadding="0" cellspacing="0" border="0" style="background-color:#f2f4f7;padding:20px 0;font-family:Arial,Helvetica,sans-serif;">
  <tr>
    <td align="center">
      <table width="600" cellpadding="0" cellspacing="0" border="0" style="background:#ffffff;border:1px solid #ddd;">
        <tr>
          <td style="padding:20px;background:#4f46e5;color:#ffffff;text-align:center;">
            <h1 style="margin:0;font-size:18px;">Password Reset Request</h1>
            <p style="margin:5px 0 0;font-size:13px;">Follow this email to continue</p>
          </td>
        </tr>
        <tr>
          <td style="padding:20px;color:#333333;font-size:14px;">
            <p style="margin:0 0 15px;">Hello <strong>%s</strong>,</p>
            <p style="margin:0 0 10px;">
              We received a request to reset the password for your account.
            </p>
            <p style="margin:0 0 20px;">Click the button below to set a new password:</p>
            <p style="text-align:center;margin:0 0 20px;">
              <a href="%s" style="display:inline-block;padding:10px 18px;background:#4f46e5;color:#ffffff;text-decoration:none;font-weight:bold;">
                Reset my password
              </a>
            </p>
            <p style="margin:15px 0 0;font-size:12px;color:#888888;">
              If you didn’t request this change, you can safely ignore this email. Your password will remain the same.
            </p>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
""", name, resetLink);
    }





}
