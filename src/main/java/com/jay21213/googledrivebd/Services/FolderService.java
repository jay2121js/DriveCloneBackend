package com.jay21213.googledrivebd.Services;

import com.jay21213.googledrivebd.DTO.AncestorProjection;
import com.jay21213.googledrivebd.DTO.FolderProjection;
import com.jay21213.googledrivebd.Entity.Folder;
import com.jay21213.googledrivebd.reprository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;

    public Folder saveFolder(Folder folder){
        return folderRepository.save(folder);
    }
    public Folder getFolder(String id){
        return folderRepository.findFolderById(id);
    }

    public List<FolderProjection> getFolders(String parentFolderId, String userId){
        return folderRepository.findProjectedByParentFolderIdAndUserId(parentFolderId, userId);
    }


    public List<AncestorProjection> getAncestors(String folderId, String userId){
        Folder folder = folderRepository.findFolderById(folderId);
        return  folderRepository.findAncestorsByIds(folder.getAncestors(), userId);
    }


    public Folder createFolder(String folderName, String userId, String parentFolderId, String folderId) {

        Folder folder = new Folder();

        // Optional: Only set ID if custom
        if (folderId != null) {
            folder.setId(folderId);
        }

        folder.setName(folderName);
        folder.setUserId(userId);
        folder.setParentFolderId(parentFolderId);// Ancestors handling
        if (parentFolderId == null) {
            folder.setAncestors(new ArrayList<>());
        } else {
            updateCount(parentFolderId,1);
            Folder parent = getFolder(parentFolderId);
            List<String> ancestors = new ArrayList<>(parent.getAncestors());
            ancestors.add(parentFolderId);
            folder.setAncestors(ancestors);
        }
        return saveFolder(folder);
    }

    public void setTempFolder(String folderId, boolean isTemp){
        int myInt = isTemp ? -1 : 1;
         Folder folder = getFolder(folderId);
         updateCount(folder.getParentFolderId(), myInt);
         folder.setTrashed(isTemp);
         saveFolder(folder);
    }

    public void setFavorite(String folderId , boolean favorite){
        Folder folder = getFolder(folderId);
        folder.setFavorite(favorite);
        saveFolder(folder);
    }

    public void updateCount(String folderId, int count){
       folderRepository.updateItemsCountSafely(folderId, count);
    }

    public List<FolderProjection> getFavorites(String userId){
        return folderRepository.findStarredFolders(userId);

    }


    public List<FolderProjection> getTrashFolder(String userId){
        return folderRepository.findTrashFolders(userId);
    }


    public void deleteAllByAncestorsContains(String folderId) {
        folderRepository.deleteAllByAncestorsContains(folderId);
    }

    public void deleteFolder(String folderId) {
        folderRepository.deleteFolderById(folderId);
    }
}
