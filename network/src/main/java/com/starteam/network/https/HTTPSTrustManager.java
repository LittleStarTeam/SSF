package com.starteam.network.https;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


/**
 * HTTPS请求绕过
 * @author Ammy
 *
 */
public class HTTPSTrustManager implements X509TrustManager {

	private Certificate ca;

	public HTTPSTrustManager() {
	}

	public HTTPSTrustManager(Certificate ca) {
		this.ca = ca;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain,
                                   String authType)
			throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain,
                                   String authType)
			throws CertificateException {
		for (X509Certificate cert : chain) {

			// Make sure that it hasn't expired.
			cert.checkValidity();

			// Verify the certificate's public key chain.
			try {
				cert.verify(ca.getPublicKey());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

}
