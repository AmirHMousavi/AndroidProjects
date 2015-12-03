package com.example.assign2.mp3player;


public class Song
{
	private final String artist;
	private final String album;
	private final String name;
	private final String path;

	private Song next = null;
	private Song previous=null;

	public Song(String artist, String album, String name, String path)
	{
		this.artist = artist;
		this.album = album;
		this.name = name;
		this.path = path;
	}

	public String getArtist()
	{
		return artist;
	}

	public String getAlbum()
	{
		return album;
	}

	public String getName()
	{
		return name;
	}

	public String getPath()
	{
		return path;
	}

	public void setNext(Song song)
	{
		next = song;
	}

	public Song getNext()
	{
		return next;
	}
	public Song getPrevious() {
		return previous;
	}

	public void setPrevious(Song song) {
		previous = song;
	}

	@Override
	public String toString()
	{
		return artist + ": " + album + " - " + name;
	}

	
}
