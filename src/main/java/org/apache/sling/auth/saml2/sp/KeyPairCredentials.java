/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.sling.auth.saml2.sp;

import org.apache.sling.auth.saml2.SAML2RuntimeException;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.x509.BasicX509Credential;
import java.security.*;
import java.security.cert.X509Certificate;

public class KeyPairCredentials extends JksCredentials {

    private KeyPairCredentials(){
        super();
    }

    public static BasicX509Credential getCredential (
            final String jksPath,
            final char[] jksPassword,
            final String certAlias,
            final char[] keysPassword) {
        try {
            KeyStore keyStore = getKeyStore(jksPath, jksPassword);
            Key key = keyStore.getKey(certAlias, keysPassword);
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(certAlias);
            PublicKey publicKey = cert.getPublicKey();
            KeyPair keyPair = new KeyPair(publicKey, (PrivateKey) key);
            return CredentialSupport.getSimpleCredential(cert,keyPair.getPrivate() );
        } catch (java.security.KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SAML2RuntimeException(e);
        }
    }
}