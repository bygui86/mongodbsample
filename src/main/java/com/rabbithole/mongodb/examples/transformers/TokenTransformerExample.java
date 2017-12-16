package com.rabbithole.mongodb.examples.transformers;

import javax.annotation.PostConstruct;

import org.bson.BSON;
import org.bson.Transformer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * from
 * http://book2s.com/java/src/package/eu/wltr/riker/meta/token/bson/tokentransformer.html#d701bad0b0d2988299378d2b89a48577
 */
// @Component
public class TokenTransformerExample {

	@PostConstruct
	public void init() {

		BSON.addEncodingHook(Token.class, new TokenEncoder());
	}

	public static class TokenEncoder implements Transformer {

		@Override
		public Object transform(final Object o) {

			if (o instanceof Token) {
				final Token token = (Token) o;
				return token.getToken();

			} else {
				return null;

			}

		}

	}
	
	@AllArgsConstructor
	@Data
	public class Token {

		private String token;

	}
}
