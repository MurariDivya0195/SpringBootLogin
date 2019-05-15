package com.bridgelabz.fundoNoteApp.user.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoNoteApp.user.model.Note;
import com.bridgelabz.fundoNoteApp.user.repository.NoteRespository;
import com.bridgelabz.fundoNoteApp.util.TokenClass;
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

	@Autowired
	NoteRespository noteRepository;

	@Autowired
	UserService userservice;

	@Autowired
	private TokenClass tokenclass;

	@Override
	public Note createNote(Note note, String token) {

		int verfifyNoteId = tokenclass.parseJWT(token);
		System.out.println(verfifyNoteId);
		Date date=new Date();
		Timestamp time= new Timestamp(date.getTime());
		note.setCreateOn(time);
		note.setId(verfifyNoteId);
		return noteRepository.save(note);
	}

	@Override
	public Note findById(int userId) {
		List<Note> noteInfo = noteRepository.findById(userId);
		return noteInfo.get(0);
	}

	@Override
	public Note updateNote(Note note, int noteId,String token) {
		int userId=tokenclass.parseJWT(token);
		List<Note> noteInfo = noteRepository.findByNoteIdAndId(noteId,userId);
		noteInfo.forEach(existingUser -> {
			existingUser.setCreateOn(note.getCreateOn() != null ? note.getCreateOn() : noteInfo.get(0).getCreateOn());
			existingUser.setDescription(
					note.getDescription() != null ? note.getDescription() : noteInfo.get(0).getDescription());
			existingUser.setTitle(note.getTitle() != null ? note.getTitle() : noteInfo.get(0).getTitle());
			/*
			 * existingUser .setUpdatedOn(note.getUpdatedOn() != null ? note.getUpdatedOn()
			 * : noteInfo.get(0).getUpdatedOn());
			 */
		});
		Date date=new Date();
		Timestamp time= new Timestamp(date.getTime());
		noteInfo.get(0).setUpdatedOn(time);
		return noteRepository.save(noteInfo.get(0));

	}

	@Override
	public String deleteNote(int noteId,String token) {
		int userId=tokenclass.parseJWT(token);

		List<Note> noteInfo = noteRepository.findByNoteIdAndId(noteId,userId);
		noteRepository.delete(noteInfo.get(0));
		return "Deleted";

	}

	@Override
	public Note getNoteInfo(int noteId) {
		List<Note> noteInfo = noteRepository.findById(noteId);
		return noteInfo.get(0);
	}

	@Override
	public List<Note> getAllNotes() {

		return noteRepository.findAll();
	}

	@Override
	public List<Note> getNotes(String token) {
		int userId=tokenclass.parseJWT(token);
		List<Note> list=noteRepository.findById(userId);
		return list;
	}

}
