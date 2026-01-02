package com.jay21213.googledrivebd.reprository;

import com.jay21213.googledrivebd.DTO.AncestorProjection;
import com.jay21213.googledrivebd.DTO.FolderProjection;
import com.jay21213.googledrivebd.Entity.Folder;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends CrudRepository<Folder, String> {

    Folder findFolderById(String id);


    void deleteAllByAncestorsContains(String folderId);

     void deleteFolderById(String folderId);


    @Query(
            value = "{ '_id': { $in: ?0 }, 'userId': ?1, 'trashed': false }",
            fields = "{ '_id': 1, 'name': 1 }"
    )
    List<AncestorProjection> findAncestorsByIds(List<String> ids, String userId);


    @Query(
            value = "{ 'parentFolderId': ?0, 'userId': ?1, 'trashed': false }",
            fields = "{ '_id': 1, 'name': 1, 'items': 1, 'favorite': 1, 'createdAt': 1, 'updatedAt': 1 }"
    )
    List<FolderProjection> findProjectedByParentFolderIdAndUserId(
            String parentFolderId,
            String userId
    );

    @Query(
            value = "{ 'userId': ?0, 'favorite': true ,'trashed': false }",
            fields = "{ '_id': 1, 'name': 1, 'items': 1, 'favorite': 1, 'createdAt': 1, 'updatedAt': 1 }"
    )
    List<FolderProjection> findStarredFolders(
            String userId
    );

    @Query(
            value = "{ 'userId': ?0, 'trashed': true }",
            fields = "{ '_id': 1, 'name': 1, 'items': 1, 'favorite': 1, 'createdAt': 1, 'updatedAt': 1 }"
    )
    List<FolderProjection> findTrashFolders(
            String userId
    );


    @Query("{ 'id': ?0 }")
    @Update(pipeline = {
            "{ '$set': { 'items': { '$max': [ { '$add': ['$items', ?1] }, 0 ] } } }"
    })
    void updateItemsCountSafely(String folderId, int count);


}
