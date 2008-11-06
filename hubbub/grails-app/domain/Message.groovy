class Message implements Comparable {

    Date dateCreated
    String fromId
    String content

    static belongsTo = [ user : User ]

    // we keep messages sorted by date on each entry
	public int compareTo(Object obj) {
		return created <=> obj.created
	}

}
