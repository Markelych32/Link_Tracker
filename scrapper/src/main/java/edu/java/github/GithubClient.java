package edu.java.github;

public interface GithubClient {
    RepositoryResponse fetchRepository(String owner, String repository);
}
