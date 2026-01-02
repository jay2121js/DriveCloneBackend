package com.jay21213.googledrivebd.Controller;

import com.jay21213.googledrivebd.Config.CustomUserDetails;
import com.jay21213.googledrivebd.DTO.*;
import com.jay21213.googledrivebd.Entity.FileItem;

import com.jay21213.googledrivebd.Entity.Folder;
import com.jay21213.googledrivebd.Services.DriveCleanupService;
import com.jay21213.googledrivebd.Services.FileItemService;
import com.jay21213.googledrivebd.Services.FolderService;
import com.jay21213.googledrivebd.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/drive")
public class DriveController {

    @Autowired
    private FileItemService fileItemService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserService userService;

    @Autowired
    private DriveCleanupService driveCleanupService;

    @Autowired
    private Executor dbExecutor;

    @GetMapping
    public DriveListResponse getFolder(
            @RequestParam(required = false) String folderId
    ) {
        CustomUserDetails user = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final String id =
                (folderId == null || folderId.isEmpty())
                        ? user.getId()
                        : folderId;

        CompletableFuture<List<FolderProjection>> foldersFuture =
                CompletableFuture.supplyAsync(
                        () -> folderService.getFolders(id, user.getId()),
                        dbExecutor
                );

        CompletableFuture<List<FileItemProjection>> filesFuture =
                CompletableFuture.supplyAsync(
                        () -> fileItemService.getAllFiles(id, user.getId()),
                        dbExecutor
                );

        CompletableFuture<List<AncestorProjection>> ancestorsFuture =
                CompletableFuture.supplyAsync(
                        () -> folderService.getAncestors(id, user.getId()),
                        dbExecutor
                );

        try {
            CompletableFuture.allOf(
                    foldersFuture,
                    filesFuture,
                    ancestorsFuture
            ).join();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch drive data", ex);
        }
        DriveListResponse response = new DriveListResponse();
        response.setFolders(foldersFuture.join());
        response.setFileItems(filesFuture.join());
        response.setAncestor(ancestorsFuture.join());
        response.setId(id);

        return response;
    }



    @PostMapping
    public ResponseEntity<FileItem> uploadFile(@RequestParam String name,
                                              @RequestParam  String folderId,
                                              @RequestParam  MultipartFile file)
    {
        CustomUserDetails auth = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  fileItemService.createFile(name, auth.getId(), folderId, file);

    }

    @PostMapping("/create")
    public ResponseEntity<Folder> CreateFile(@RequestParam String folderName, @RequestParam String parentFolderId){
       CustomUserDetails auth = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Folder folder = folderService.createFolder(folderName, auth.getId(), parentFolderId, null);
       return ResponseEntity.ok(folder);
    }

    @GetMapping("ok")
    public String checkFile(){
        return "ok";
    }

    @PutMapping("/folder/temp")
    public void DeleteFolder(@RequestParam String folderId, @RequestParam boolean isTemp){
        folderService.setTempFolder(folderId, isTemp);
    }

    @PutMapping("/folder/favourite")
    public void favouriteFolder(@RequestParam String folderId,@RequestParam boolean isfavourite){
        folderService.setFavorite(folderId, isfavourite);
    }

    @PutMapping("/file/temp")
    public void Deletefile(@RequestParam String fileId, @RequestParam boolean isTemp){
        fileItemService.setTempFolder(fileId, isTemp);
    }

    @PutMapping("/file/favourite")
    public void favouriteFile(@RequestParam String fileId,@RequestParam boolean isfavourite){
        fileItemService.setFavorite(fileId, isfavourite);
    }

    @GetMapping("/favourite")
    public ResponseEntity<DriveListResponse> getFavourite() {

        CustomUserDetails auth = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CompletableFuture<List<FolderProjection>> foldersFuture =
                CompletableFuture.supplyAsync(
                        () -> folderService.getFavorites(auth.getId()),
                        dbExecutor
                );

        CompletableFuture<List<FileItemProjection>> filesFuture =
                CompletableFuture.supplyAsync(
                        () -> fileItemService.getFavorites(auth.getId()),
                        dbExecutor
                );

        CompletableFuture.allOf(
                foldersFuture,
                filesFuture
        ).join();

        DriveListResponse response = new DriveListResponse();
        response.setFolders(foldersFuture.join());
        response.setFileItems(filesFuture.join());
        response.setAncestor(new ArrayList<>());
        response.setId(auth.getId());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/Trash")
    public ResponseEntity<DriveListResponse> getTrash() {

        CustomUserDetails auth = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CompletableFuture<List<FolderProjection>> foldersFuture =
                CompletableFuture.supplyAsync(
                        () -> folderService.getTrashFolder(auth.getId()),
                        dbExecutor
                );

        CompletableFuture<List<FileItemProjection>> filesFuture =
                CompletableFuture.supplyAsync(
                        () -> fileItemService.getTrashFile(auth.getId()),
                        dbExecutor
                );

        CompletableFuture.allOf(
                foldersFuture,
                filesFuture
        ).join();

        DriveListResponse response = new DriveListResponse();
        response.setFolders(foldersFuture.join());
        response.setFileItems(filesFuture.join());
        response.setAncestor(new ArrayList<>());
        response.setId(auth.getId());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/Size")
    public ResponseEntity<UserData> getSize(){
        CustomUserDetails auth = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getUserData(auth.getId()));
    }

    @DeleteMapping("/file/delete")
    public ResponseEntity<Boolean> deleteFile(@RequestParam String fileId){
        CustomUserDetails auth = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return fileItemService.deleteFile(fileId, auth.getId());
    }

    @DeleteMapping("/folder/delete")
    public ResponseEntity<Boolean> deleteFolder(@RequestParam String folderId){
        CustomUserDetails auth = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driveCleanupService.deleteFolderPermanently(folderId, auth.getId());
    }
}
