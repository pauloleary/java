package com.ariba.clt.git.util.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;

import com.ariba.clt.testutil.FileTestUtil;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.Status;
import org.junit.Test;
import com.ariba.clt.util.FileSystemUtil;

import junit.framework.Assert;

public class GitServiceTest
{

    @Test
    public void clone_successCase () throws GitAPIException, IOException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void areGitHubDetailsProvided_successCase ()
        throws GitAPIException, IOException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(gitService.hasGitHubRepoSshUri());
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void accessLocalGit_successCase () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        GitService gitService2 = GitService.getGitService(localRepoPath);
        assertTrue(gitService2.hasGitObject());
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void checkout_WithRemoteBranch () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(gitService.checkout("develop"));
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void checkout_ExistingCheckedOutBranch () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(gitService.checkout("develop"));
        assertTrue(gitService.checkout("develop"));
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void checkout_WithoutRemoteBranch () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(gitService.checkout("no-such-branch"));
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void checkout_NullBranchName () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(!gitService.checkout(null));
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void checkout_EmptyBranchName () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(!gitService.checkout(""));
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void checkout_ExistingBranches () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(gitService.checkout("develop"));
        assertTrue(gitService.checkout("blah"));
        assertTrue(gitService.checkout("develop"));
        assertTrue(gitService.checkout("blah"));
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    /**
     * Test checking out an existing commit id.
     * Test checking out a non-existing commit id.
     * @throws IOException
     * @throws GitAPIException
     */
    @Test
    public void checkoutToCommitId () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        String commitId = "cf7ec829c7eddb3d1619a401923a91f638c3dd09";
        try {
            GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
            boolean success = gitService.checkout("develop", commitId);
            assertTrue(success);
            String commitIdBad = "foo";
            success = gitService.checkout("develop", commitIdBad);
            assertFalse(success);
        }
        finally {
            FileSystemUtil.deleteDirectory(new File(localRepoPath));
        }
    }

    @Test
    public void pull_SuccessCase () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
        assertTrue(gitService.pull());
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void addFile_GetAddedStatusSuccessCase () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);

        assertTrue(gitService.checkout("master"));

        File testFile = new File(localRepoPath + File.separator + "test.strings");
        if (!testFile.isFile()) {
            testFile.createNewFile();
        }
        assertTrue(gitService.add("test.strings"));
        Status status = gitService.status();
        assertTrue(status.getAdded().size() == 1);
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void commitFileSuccessCase () throws IOException, GitAPIException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);

        assertTrue(gitService.checkout("master"));

        File testFile = new File(localRepoPath + File.separator + "test3.strings");
        if (!testFile.isFile()) {
            testFile.createNewFile();
        }
        assertTrue(gitService.add("test3.strings"));
        assertTrue(gitService.commit("Test Commit"));
        Status status = gitService.status();
        assertTrue(status.hasUncommittedChanges() == false);
        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    @Test
    public void cleanUntrackedFilesAndDirectories_successCase ()
        throws GitAPIException, IOException
    {
        String gitHubRepoSshUri =
            "git@github.wdf.sap.corp:Ariba-l10n/com.ariba.clt.git.util";
        String localRepoPath = "./test-repo";
        GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);

        // Now create a directory and a file within it.
        String untrackedDirectoryPath = "./test-repo/untrackedDirectory";
        String untrackedFilePath =
            String.format("%s/untrackedFile", untrackedDirectoryPath);

        File parentFolder = new File(untrackedDirectoryPath);
        parentFolder.mkdirs();
        FileTestUtil.createFileWithSpecifiedLineCountOnDisk(untrackedFilePath, 7);

        Status status = gitService.status();

        // Check that the untracked file/folder is present.
        assertEquals(1, status.getUntracked().size());
        assertEquals(1, status.getUntrackedFolders().size());

        gitService.cleanUntrackedFilesAndDirectories();

        status = gitService.status();

        // Check that the untracked file/folder is not present.
        assertEquals(0, status.getUntracked().size());
        assertEquals(0, status.getUntrackedFolders().size());

        FileSystemUtil.deleteDirectory(new File(localRepoPath));
    }

    /**
     * Test positive with and existing tag Test negative with a non-existent tag
     * 
     * @throws GitAPIException
     */
    @Test
    public void getCommitIdForTag () throws GitAPIException
    {
        String gitHubRepoSshUri = "git@github.wdf.sap.corp:Ariba-PD/procurement-desk.git";
        String localRepoPath = "./test-repo";
        try {
            GitService gitService = GitService.cloneRepo(gitHubRepoSshUri, localRepoPath);
            String commitId = gitService.findCommitIdForTag("v1.3.0");
            Assert.assertEquals("8bdb2389b9611385874200bbc5b9e96e5eff2193", commitId);
            commitId = gitService.findCommitIdForTag("foo");
            Assert.assertNull(commitId);
        }
        finally {
            FileSystemUtil.deleteDirectory(new File(localRepoPath));
        }
    }
}
