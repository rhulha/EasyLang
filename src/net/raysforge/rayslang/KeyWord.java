package net.raysforge.rayslang;


public enum KeyWord {
	
	PUBLIC("publik"),
	NEW("neu");
	
	private final String localText;

	private KeyWord( String localText)
	{
		this.localText = localText;
	}
	
	public String getLocalText() {
		return localText;
	}
	
	public static KeyWord getKeyWordfromLocalText(String localText)
	{
		for (KeyWord tile : KeyWord.values()) {
			if( tile.getLocalText().equals(localText))
				return tile;
		}
		return null;
	}

}
