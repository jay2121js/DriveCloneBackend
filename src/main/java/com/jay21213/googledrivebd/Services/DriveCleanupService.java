package com.jay21213.googledrivebd.Services;

import com.jay21213.googledrivebd.Entity.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DriveCleanupService {

    @Autowired
    private FileItemService fileItemService;
    @Autowired
    private FolderService folderService;



    public ResponseEntity<Boolean> deleteFolderPermanently(
            String folderId,
            String userId
    ) {
        Folder folder = folderService.getFolder(folderId);
        if (folder == null || !folder.getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        // 1. Delete files (Cloudinary + DB)
        fileItemService.deleteFileItem(folderId);

        // 2. Delete subfolders
        folderService.deleteAllByAncestorsContains(folderId);

        // 3. Delete folder itself
        folderService.deleteFolder(folderId);

        return ResponseEntity.ok(true);
    }
}
