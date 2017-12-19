# Workflow

To avoid a huge number of commits for a very small amount of changes the following workflow is best practice for GitPitch:

1. Create a new branch (e.g. from `master`):

```bash
git checkout -b slides-<any-suffix>
```

It's strongly recommended to avoid `/` in the name of the branch because GitPitch does not handle this.
The suffix does not really matter it's just used to avoid naming conflicts if multiple persons are working on the slides at the same time.

2. do the work you have to do :wink:
3. switch back to the branch from where you started (e.g. `master`)

```bash
git checkout master
```

4. do a squash merge

```bash
git merge --squash slides-<your-suffix>
git commit -m "meaningful message"
git push
```

Now your changes are on the original branch with a single commit.

5. cleanup

```bash
# remove local branch
git branch -d slides-<your-suffix>
# remove remote branch
git push -d slides-<your-suffix>
```
