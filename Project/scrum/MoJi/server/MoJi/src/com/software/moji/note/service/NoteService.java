package com.software.moji.note.service;

import java.sql.SQLException;
import java.util.List;

import com.software.moji.entity.Note;
import com.software.moji.note.dao.NoteDao;

public class NoteService {
	public Note queryUserId(String noteId) throws SQLException {
		return new NoteDao().queryUserId(noteId);
	}
	
	public List<Note> queryVisualNote() throws SQLException{
		return new NoteDao().queryVisualNote();
	}

	public List<Note> DownloadNote(String userId) throws SQLException{
		return new NoteDao().queryNote(userId);
	} 
	
	public List<Note> findNote(String userId, double left, double right, double top, double bottom) throws SQLException {
		return new NoteDao().findByRange(userId, left, right, top, bottom);
	}
	
	public int addNote(List<String> pathlist, String id, String userId, String title, String content, double latitude, double longitude, String location, String time, int self) {
		return new NoteDao().addNote(pathlist, id, userId, title, content, latitude, longitude, location, time, self);
	}
	
	public Note checkNote(String userId, double left, double right, double top, double bottom) {
		return new NoteDao().checkNote(userId, left, right, top, bottom);
	}
	
	public int updateNote(List<String> pathlist, Note note, String id, String userId, String title, String content, double latitude, double longitude, String location, String time, int self) {
		return new NoteDao().updateNote(pathlist, note,id, userId, title, content, latitude, longitude, location, time, self);
	}
	
	public boolean deleteNote(String noteId) throws SQLException {
		return new NoteDao().deleteNote(noteId);
	}

}
