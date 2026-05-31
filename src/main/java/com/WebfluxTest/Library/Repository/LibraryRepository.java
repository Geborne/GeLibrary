package com.WebfluxTest.Library.Repository;

import com.WebfluxTest.Library.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface LibraryRepository extends ReactiveMongoRepository <Book, String> {

}
