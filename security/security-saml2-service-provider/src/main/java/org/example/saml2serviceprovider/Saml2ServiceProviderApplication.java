package org.example.saml2serviceprovider;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints({Saml2ServiceProviderApplication.Hints.class})
public class Saml2ServiceProviderApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Saml2ServiceProviderApplication.class, args);
		Thread.currentThread().join(); // To be able to measure memory consumption
	}

	static class Hints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			hints.resources().registerPattern("credentials/*.crt").registerPattern("credentials/*.key");
			hints.resources().registerPattern("default-config.xml").registerPattern("schema-config.xml").registerPattern("encryption-config.xml");
			List<TypeReference> typeReferences = Stream.of(TypeReference.of("com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl"), TypeReference.of("org.apache.xml.security.algorithms.implementations.SignatureBaseRSA$SignatureRSASHA256"),
					TypeReference.of("org.apache.xml.security.transforms.implementations.TransformC14NExclusive"), TypeReference.of("org.apache.xml.security.transforms.implementations.TransformEnvelopedSignature"),
					TypeReference.of("org.opensaml.security.criteria.UsageCriterion"),
					TypeReference.of("org.opensaml.security.criteria.KeyLengthCriterion"),
					TypeReference.of("org.opensaml.security.criteria.KeyNameCriterion"),
					TypeReference.of("org.opensaml.security.criteria.KeyAlgorithmCriterion"),
					TypeReference.of("org.opensaml.core.criterion.EntityIdCriterion"),
					TypeReference.of("org.opensaml.security.x509.X509SubjectKeyIdentifierCriterion"),
					TypeReference.of("org.opensaml.security.x509.X509DigestCriterion"),
					TypeReference.of("org.opensaml.security.x509.X509SubjectNameCriterion"),
					TypeReference.of("org.opensaml.security.criteria.PublicKeyCriterion"),
					TypeReference.of("org.opensaml.security.x509.X509IssuerSerialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableUsageCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableKeyLengthCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableKeyNameCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableKeyAlgorithmCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableEntityIDCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableX509SubjectKeyIdentifierCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableX509DigestCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableX509SubjectNameCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluablePublicKeyCredentialCriterion"),
					TypeReference.of("org.opensaml.security.credential.criteria.impl.EvaluableX509IssuerSerialCredentialCriterion"),
					TypeReference.of("org.opensaml.core.xml.schema.impl.XSAnyBuilder"),
					TypeReference.of("org.opensaml.core.xml.schema.impl.XSAnyMarshaller"), TypeReference.of("org.opensaml.core.xml.schema.impl.XSAnyUnmarshaller")).toList();
			hints.reflection().registerTypes(typeReferences, (builder) -> builder.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));
			hints.reflection().registerType(TypeReference.of("org.springframework.security.saml2.core.OpenSamlInitializationService"), (builder) -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
		}

	}

}
