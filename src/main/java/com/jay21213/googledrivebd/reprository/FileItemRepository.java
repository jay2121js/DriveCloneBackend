package com.jay21213.googledrivebd.reprository;

import com.jay21213.googledrivebd.DTO.FileItemProjection;
import com.jay21213.googledrivebd.DTO.FilesData;
import com.jay21213.googledrivebd.Entity.FileItem;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileItemRepository extends CrudRepository<FileItem, String> {

    FileItem findFileItemById(String fileId);

    @Query(
            value = "{ 'folderId': ?0, 'userId': ?1, 'trashed': false, }",
            fields = "{ '_id': 1, 'name': 1, 'size': 1, 'favorite': 1, 'mimeType': 1, 'updatedAt': 1, 'createdAt': 1, 'cloudinaryUrl': 1 }"
    )
    List<FileItemProjection> findProjectedByFolderIdAndUserId(
            String folderId,
            String userId
    );

    @Query(
            value = "{ 'ancestors': { $in: [?0] }}",
            fields = "{ 'cloudinaryPublicId': 1, 'cloudinaryType': 1 }"
    )
    List<FilesData> findFileDataByFolderId(String folderId);



    void deleteAllByAncestorsContains(String id);

    @Query(
            value = "{ 'userId': ?0, 'favorite': true,'trashed': false}",
            fields = "{ '_id': 1, 'name': 1, 'size': 1, 'favorite': 1, 'mimeType': 1, 'updatedAt': 1, 'createdAt': 1, 'cloudinaryUrl': 1 }"
    )
    List<FileItemProjection> findStarredFile(
            String userId
    );

    @Query(
            value = "{ 'userId': ?0, 'trashed': true}",
            fields = "{ '_id': 1, 'name': 1, 'size': 1, 'favorite': 1, 'mimeType': 1, 'updatedAt': 1, 'createdAt': 1, 'cloudinaryUrl': 1 }"
    )
    List<FileItemProjection> findTrashed(
            String userId
    );

}
