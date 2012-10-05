/**
 * Copyright 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tonicsystems.jarjar;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.jar.JarFile;

public class EmptyAndNotEmptyDirsIT extends AbstractMojoTestCase {

    private File testDir = getTestFile("src/test/resources/projects/empty_dirs");
    private Verifier verifier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        verifier = new Verifier(testDir.getAbsolutePath());
        verifier.executeGoals(Arrays.asList(new String[]{"clean", "package"}));
        verifier.displayStreamBuffers();
        verifier.resetStreams();
    }

    public void testDirectoriesWithFilesAreIncluded() throws VerificationException, IOException {
        assertPresentInJar("target/out.jar", "test/has_file/");
    }

    public void testDirectoriesWithoutFilesAreExcluded() throws VerificationException, IOException {
        assertNotPresentInJar("target/out.jar", "test/empty/");
    }

    public void testDirectoriesCreatedByRulesAreIncluded() throws VerificationException, IOException {
        assertPresentInJar("target/out.jar", "com/test/junit/");
    }

    private void assertPresentInJar(String jarFile, String entry) throws IOException {
        JarFile file = getJar(jarFile);
        assertNotNull("Path "+entry+" should be in jar "+jarFile, file.getEntry(entry));
    }

    private void assertNotPresentInJar(String jarFile, String entry) throws IOException {
        JarFile file = getJar(jarFile);
        assertNull("Path " + entry + " should not be in jar " + jarFile, file.getEntry(entry));
    }

    private JarFile getJar(String jarFile) throws IOException {
        return new JarFile(new File(testDir, jarFile).getAbsolutePath());
    }

}
