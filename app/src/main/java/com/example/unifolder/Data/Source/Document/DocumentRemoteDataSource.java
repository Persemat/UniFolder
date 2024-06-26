package com.example.unifolder.Data.Source.Document;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unifolder.Model.Document;
import com.example.unifolder.Ui.Main.UploadDocumentCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DocumentRemoteDataSource {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private final CollectionReference documentsCollection = db.collection("documents");
    private final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    private final String TAG = DocumentRemoteDataSource.class.getSimpleName();

    public ListenableFuture<List<Document>> searchDocumentsByTitle(String searchQuery) {
        SettableFuture<List<Document>> future = SettableFuture.create(); // Utilizziamo SettableFuture per creare un futuro modificabile

        Query query = documentsCollection.whereGreaterThanOrEqualTo("title", searchQuery)
                .whereLessThanOrEqualTo("title", searchQuery + "\uf8ff");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Document> matchingDocuments = new ArrayList<>();
                    for (int i = 0; i < task.getResult().size(); i++) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(i);
                        Log.d(TAG,"found doc");
                        Document doc = document.toObject(Document.class);
                        String id = task.getResult().getDocuments().get(i).getId();
                        doc.setId(id);
                        matchingDocuments.add(doc);
                    }
                    future.set(matchingDocuments); // Imposta il risultato del futuro con i documenti corrispondenti
                } else {
                    future.setException(task.getException()); // Imposta un'eccezione nel futuro in caso di errore
                }
            }
        });

        Log.d(TAG,"returning from searchDocumentsByTitle()");
        return future;
    }

    public ListenableFuture<List<Document>> searchDocumentsByCourseAndTag(String course, String tag) {
        SettableFuture<List<Document>> future = SettableFuture.create(); // Utilizziamo SettableFuture per creare un futuro modificabile

        Query query = documentsCollection.whereEqualTo("course", course);
        /*  TODO: case-insensitive version?
        Query query = documentsCollection
                .orderBy("title", Query.Direction.ASCENDING)  // Ordina i risultati per titolo in modo case-insensitive
                .startAt(course.toLowerCase())           // Fai partire la ricerca dal termine in minuscolo
                .endAt(course.toLowerCase() + "\uf8ff"); // Termina la ricerca al termine in minuscolo seguito da un carattere speciale*/


        // Se specificato, aggiungi la clausola per il tag
        if (tag != null && !tag.isEmpty()) {
            query = query.whereEqualTo("tag", tag);
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Document> matchingDocuments = new ArrayList<>();
                    for (int i = 0; i < task.getResult().size(); i++) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(i);
                        Log.d(TAG,"found doc");
                        Document doc = document.toObject(Document.class);
                        String id = task.getResult().getDocuments().get(i).getId();
                        doc.setId(id);
                        matchingDocuments.add(doc);
                    }
                    future.set(matchingDocuments); // Imposta il risultato del futuro con i documenti corrispondenti
                } else {
                    future.setException(task.getException()); // Imposta un'eccezione nel futuro in caso di errore
                }
            }
        });

        Log.d(TAG,"returning from searchDocumentsByCourseAndTag()");
        return future;
    }

    public ListenableFuture<List<Document>> searchDocumentsByTitleAndFilter(String searchQuery, String course, String tag) {
        SettableFuture<List<Document>> future = SettableFuture.create(); // Utilizziamo SettableFuture per creare un futuro modificabile

        Query query = documentsCollection.whereGreaterThanOrEqualTo("title", searchQuery)
                .whereLessThanOrEqualTo("title", searchQuery + "\uf8ff");

        if(course != null && !course.isEmpty())
            query = query.whereEqualTo("course", course);


        /*  TODO: case-insensitive version?
        Query query = documentsCollection
                .orderBy("title", Query.Direction.ASCENDING)  // Ordina i risultati per titolo in modo case-insensitive
                .startAt(course.toLowerCase())           // Fai partire la ricerca dal termine in minuscolo
                .endAt(course.toLowerCase() + "\uf8ff"); // Termina la ricerca al termine in minuscolo seguito da un carattere speciale*/


        // Se specificato, aggiungi la clausola per il tag
        if (tag != null && !tag.isEmpty()) {
            query = query.whereEqualTo("tag", tag);
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Document> matchingDocuments = new ArrayList<>();
                    for (int i = 0; i < task.getResult().size(); i++) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(i);
                        Log.d(TAG,"found doc");
                        Document doc = document.toObject(Document.class);
                        String id = task.getResult().getDocuments().get(i).getId();
                        doc.setId(id);
                        matchingDocuments.add(doc);
                    }
                    future.set(matchingDocuments); // Imposta il risultato del futuro con i documenti corrispondenti
                } else {
                    future.setException(task.getException()); // Imposta un'eccezione nel futuro in caso di errore
                }
            }
        });

        Log.d(TAG,"returning from searchDocumentsByTitleAndFilter()");
        return future;
    }

    public Task<Uri> uploadDocument(Document document, UploadDocumentCallback uploadDocumentCallback) {
        // Ottieni un riferimento al percorso nel Cloud Storage
        String fileName = "document_" + document.getTitle() + ".pdf"; // Nome del file nel Cloud Storage
        StorageReference fileRef = storage.getReference().child("documents").child(fileName);

        // Carica il file su Firebase Cloud Storage
        Uri fileUri = Uri.parse(document.getFileUrl());
        UploadTask uploadTask = fileRef.putFile(fileUri);

        // Continua con il completamento dell'uploadTask per ottenere l'URL del file
        return fileRef.putFile(fileUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "!task.isSuccessful()");
                    throw task.getException();
                }

                // Ottieni l'URL del file caricato
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete -> task.isSuccessful()");

                    // Ottieni l'URL del file
                    Uri downloadUri = task.getResult();

                    // Esegui l'operazione di accesso al database Room su un thread diverso
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            // Aggiungi il documento al database Firestore con l'URL del file
                            Document remoteDocument = new Document(document.getTitle(), document.getAuthor(), document.getCourse(), document.getTag(), "");
                            remoteDocument.setFileUrl(downloadUri.toString());

                            // Aggiungi il documento al database Firestore
                            try {
                                documentsCollection.add(remoteDocument).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        DocumentReference docRef = task.getResult();
                                        String documentId = docRef.getId();

                                        // Aggiorna il documento locale con l'ID generato da Firebase
                                        document.setId(documentId);
                                        document.setFileUrl(remoteDocument.getFileUrl());
                                        Log.d(TAG,"docRef path: "+document.getFileUrl());
                                        Log.d(TAG,"remoteDoc path: "+remoteDocument.getFileUrl());
                                        uploadDocumentCallback.onDocumentUploaded(document);
                                    }
                                });

                            } catch (Exception e) {
                                Log.e(TAG, "Errore durante l'aggiunta del documento:", e);
                                uploadDocumentCallback.onUploadFailed(e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "onComplete -> !task.isSuccessful()");

                    // Gestisci l'errore durante il caricamento del file
                    Exception e = task.getException();
                    Log.e(TAG, "Errore durante il caricamento del file:", e);
                    uploadDocumentCallback.onUploadFailed(e.getMessage());
                }
            }
        });
    }
}
