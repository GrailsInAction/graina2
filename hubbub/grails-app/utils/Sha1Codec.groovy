// Calculate Sha1 Digest
class Sha1Codec {
	static encode = { str ->
		def md = java.security.MessageDigest.getInstance("SHA1");
		def dig = md.digest(str.toString().bytes);
		return dig.encodeBase64().toString();
	}

}