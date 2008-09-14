class Tag implements Comparable {
	
	String name
	
	User user
	
	static hasMany = [ posts : Post ]
	
	//NB owned by post, so you can only do post.addToTags() not other way around
	static belongsTo = [ User, Post, StarPost ]
	
	
	// we keep tags sorted by tag name on each entry
	public int compareTo(Object obj) {
		return name <=> obj.name
	}

}
