package com.jay21213.googledrivebd.Services;


import com.jay21213.googledrivebd.DTO.FileItemProjection;
import com.jay21213.googledrivebd.DTO.FilesData;
import com.jay21213.googledrivebd.Entity.FileItem;
import com.jay21213.googledrivebd.Entity.Folder;
import com.jay21213.googledrivebd.Entity.User;
import com.jay21213.googledrivebd.reprository.FileItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class FileItemService {
    @Autowired
    FolderService folderService;
    @Autowired
    FileItemRepository fileItemRepository;

    @Autowired
    CloudinarySerivce cloudinaryService;
    @Autowired
    private UserService userService;

    public List<FileItemProjection> getAllFiles(String folderid, String userId) {
        return fileItemRepository.findProjectedByFolderIdAndUserId(folderid,  userId);
    }

    public FileItem getFileItem(String fileId) {
        return  fileItemRepository.findFileItemById(fileId);
    }

    public FileItem saveFileItem(FileItem fileItem) {
        return fileItemRepository.save(fileItem);
    }


    public ResponseEntity<FileItem> createFile(
            String name,
            String userId,
            String folderId,
            MultipartFile file
    ) {
        try{
            Folder folder = folderService.getFolder(folderId);
            ArrayList<String> folderAncestors = new ArrayList<>(folder.getAncestors());
            folderAncestors.add(folderId);
            folderService.updateCount(folderId,1);
            if (file==null) return ResponseEntity.badRequest().build();
                Map<String, String> map = cloudinaryService.uploadFile(file);
                FileItem fileItem = FileItem.builder()
                        .name(name)
                        .userId(userId)
                        .folderId(folderId)
                        .cloudinaryPublicId(map.get("public_id"))
                        .cloudinaryUrl(map.get("secure_url"))
                        .mimeType(file.getContentType())
                        .cloudinaryType(map.get("resource_type"))
                        .ancestors(folderAncestors)
                        .size(Long.valueOf(map.get("size")))
                        .build();
                   User user=  userService.getUserById(userId);
                   user.setUsedStorage(user.getUsedStorage()+fileItem.getSize());
                   userService.saveUser(user);
                return ResponseEntity.ok(saveFileItem(fileItem));
      }catch (Exception ex){
            log.error(ex.getMessage(),ex);
        }
      return  ResponseEntity.notFound().build();
    }


    public void setTempFolder(String id, boolean isTemp){
        int myInt = isTemp ? -1 : 1;
        FileItem fileItem = getFileItem(id);
        folderService.updateCount(fileItem.getFolderId(),myInt);
        fileItem.setTrashed(isTemp);
        saveFileItem(fileItem);
    }

    public void setFavorite(String id , boolean favorite){
        FileItem fileItem = getFileItem(id);
        fileItem.setFavorite(favorite);
        saveFileItem(fileItem);
    }

    public List<FileItemProjection> getFavorites(String userId){
        return fileItemRepository.findStarredFile(userId);
    }

    public List<FileItemProjection> getTrashFile(String userId){
        return fileItemRepository.findTrashed(userId);
    }

    public ResponseEntity<Boolean> deleteFile(String fileId, String userId) {
        try{
            FileItem FileItem = fileItemRepository.findFileItemById(fileId);
            if (FileItem==null || FileItem.getUserId() == userId) return ResponseEntity.notFound().build();
            cloudinaryService.deleteFile(FileItem.getCloudinaryPublicId(), FileItem.getCloudinaryType());
            fileItemRepository.delete(FileItem);
            return ResponseEntity.ok(true);
        }catch (Exception ex){
            log.error(ex.getMessage(),ex);
            return ResponseEntity.notFound().build();
        }
    }


    void deleteFileItem(String folderId) {
       List<FilesData> file = fileItemRepository.findFileDataByFolderId(folderId);
       for (FilesData fileItem : file) {
           cloudinaryService.deleteFile(fileItem.getCloudinaryPublicId(), fileItem.getCloudinaryType());
       }
        fileItemRepository.deleteAllByAncestorsContains(folderId);
    }



}
