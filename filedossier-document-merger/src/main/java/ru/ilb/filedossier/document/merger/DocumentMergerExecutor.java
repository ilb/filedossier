package ru.ilb.filedossier.document.merger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Allow to merge bunch of documents. They will be merged by order in list
 * documents.
 */
public class DocumentMergerExecutor {

   private List<byte[]> documents;
   private DocumentMergerFactory factory = DocumentMergerFactory.getInstance();

   /**
    * Constructor for empty initialization, if we haven't documents list already.
    */
   private DocumentMergerExecutor() {
      this.documents = new ArrayList<>();
   }

   /**
    * Constructor for initialization with documents list. Whatever, we can add some documents
    * with method @<code>addDocumentToMerge</code>
    * @param documents list with binary documents, which needs to be merged
    */
   private DocumentMergerExecutor(List<byte[]> documents) {
      this.documents = documents;
   }

   public static DocumentMergerExecutor getInstance() {
      return new DocumentMergerExecutor();
   }

   /**
    * Initializes DocumentMergerExecutor with list, added just for better readability.
    * @param rawDocuments list with binary documents, which needs to be merged
    * @return executor
    */
   public static DocumentMergerExecutor fromRawList(List<byte[]> rawDocuments) {
      return new DocumentMergerExecutor(rawDocuments);
   }

   public void addDocumentToMerge(byte[] document) {
      documents.add(document);
   }

   public void addDocumentToMerge(File document) {
      try {
         documents.add(Files.readAllBytes(document.toPath()));
      } catch (IOException e) {
         throw new RuntimeException("Unable to add document to merge chain: " + e);
      }
   }

   /**
    * The same method as @<code>fromRawList</code>, but allows use documents in File
    * @param documents list with File documents, which needs to be merged
    * @return executor
    */
   public static DocumentMergerExecutor fromList(List<File> documents) {
      List<byte[]> rawFiles = documents.stream().map(document -> {
         try { return Files.readAllBytes(document.toPath()); } catch (IOException e) {
            throw new RuntimeException("Unable to read incoming files to merge: " + e);
         }
      }).collect(Collectors.toList());
      return fromRawList(rawFiles);
   }

   /**
    * Merges all specified documents one by one.
    * @return merged document
    */
   public byte[] executeMerge() {
      byte[] result = documents.get(0);
      documents.remove(0);
      for(byte[] document : documents) {
         result = factory.getDocumentMerger(result, document).apply(result, document);
      }
      return result;
   }
}
