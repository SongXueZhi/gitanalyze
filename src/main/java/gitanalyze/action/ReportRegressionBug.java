package gitanalyze.action;

import java.io.IOException;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import gitanalyze.until.RepositoryUtil;


public class ReportRegressionBug {

	public void searchRegressionBug(String repoPath) throws IOException {
		
		Repository repository = RepositoryUtil.openRepository(repoPath);
		ObjectId lastCommitId = repository.resolve(Constants.HEAD);
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = walk.parseCommit(lastCommitId);
		walk.markStart(commit);
		RevCommit postCommit = commit;
		for (RevCommit prevCommit : walk) {
			if (prevCommit == postCommit) {
				continue;
			}
			System.out.print(prevCommit.getFullMessage());
		}
	}
    
}
