package com.boot.first.demo.service;

import com.boot.first.demo.entity.JournalEntry;
import com.boot.first.demo.entity.UserEntity;
import com.boot.first.demo.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    private final JournalEntryRepository repository;
    private final UserService userService;
    private final JournalEntryRepository journalEntryRepository;

    @Autowired
    public JournalEntryService(JournalEntryRepository repository, UserService userService, JournalEntryRepository journalEntryRepository) {
        this.repository = repository;
        this.userService = userService;
        this.journalEntryRepository = journalEntryRepository;
    }

    @Transactional
    public void saveEntry(JournalEntry entry, String user) {
        entry.setDate(LocalDateTime.now());
        JournalEntry save = repository.save(entry);
        Optional<UserEntity> userEntity = userService.findByName(user);
        userEntity.get().getJournalEntries().add(save);
        userService.save(userEntity.get());
    }


    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getJournalEntries() {
        return repository.findAll();
    }

    public Optional<JournalEntry> getJournalEntry(ObjectId id) {
        return repository.findById(id);
    }

    public void deleteJournalEntry(ObjectId id, String user) {
        Optional<UserEntity> userJournalDelete = userService.findByName(user);
        userJournalDelete.get().getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        userService.save(userJournalDelete.get());
        repository.findById(id).ifPresent(repository::delete);
    }
}
