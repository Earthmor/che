/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.commons.lang.execution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages command line options and create native OS process. Also manages creating {@link Process}
 * object by {@link CommandLine#createProcess()}. All options represented as-is, without any
 * escaping or encoding.
 *
 * @author Evgen Vidolob
 */
public class CommandLine {
  private static final Logger LOG = LoggerFactory.getLogger(CommandLine.class);
  private String executablePath;
  private boolean redirectErrorStream;
  private File workingDirectory;

  private Map<String, String> environment = new HashMap<>();
  private List<String> parameters = new ArrayList<>();

  public CommandLine() {}

  public CommandLine(String... command) {
    this(Arrays.asList(command));
  }

  public CommandLine(List<String> command) {
    if (command.size() > 0) {
      executablePath = command.get(0);
      if (command.size() > 1) {
        addParameters(command.subList(0, command.size()));
      }
    }
  }

  public String getExecutablePath() {
    return executablePath;
  }

  public void setExecutablePath(String executablePath) {
    this.executablePath = executablePath;
  }

  public void addParameters(List<String> parameters) {
    this.parameters.addAll(parameters);
  }

  public File getWorkingDirectory() {
    return workingDirectory;
  }

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public void setWorkingDirectory(String path) {
    if (path != null && !path.isEmpty()) {
      workingDirectory = new File(path);
    }
  }

  public Map<String, String> getEnvironment() {
    return environment;
  }

  public boolean isRedirectErrorStream() {
    return redirectErrorStream;
  }

  public void setRedirectErrorStream(boolean redirectErrorStream) {
    this.redirectErrorStream = redirectErrorStream;
  }

  public Process createProcess() throws ExecutionException {
    List<String> commandLine = createCommandLine();
    try {
      return runProcess(commandLine);
    } catch (IOException e) {
      throw new ExecutionException(e);
    }
  }

  private Process runProcess(List<String> commandLine) throws IOException {
    LOG.info("Start process {} environment {} ", commandLine, environment);
    ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
    Map<String, String> environment = processBuilder.environment();
    environment.clear();
    environment.putAll(this.environment);
    processBuilder.directory(workingDirectory);
    processBuilder.redirectErrorStream(redirectErrorStream);
    return processBuilder.start();
  }

  private List<String> createCommandLine() {
    List<String> commandLine = new ArrayList<>(parameters.size() + 1);
    commandLine.add(executablePath);
    commandLine.addAll(parameters);
    return commandLine;
  }

  public void addParameter(String parameter) {
    parameters.add(parameter);
  }
}
