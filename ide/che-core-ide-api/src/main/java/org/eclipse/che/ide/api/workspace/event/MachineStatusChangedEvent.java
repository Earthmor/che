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
package org.eclipse.che.ide.api.workspace.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.eclipse.che.api.core.model.workspace.runtime.MachineStatus;

/** Fired when state of machine has been changed. */
public class MachineStatusChangedEvent extends GwtEvent<MachineStatusChangedEvent.Handler> {

  public static final Type<MachineStatusChangedEvent.Handler> TYPE = new Type<>();

  private String machineName;

  private String workspaceId;

  private MachineStatus status;

  public MachineStatusChangedEvent(String machineName, String workspaceId, MachineStatus status) {
    this.machineName = machineName;
    this.workspaceId = workspaceId;
    this.status = status;
  }

  public String getMachineName() {
    return machineName;
  }

  public String getWorkspaceId() {
    return workspaceId;
  }

  public MachineStatus getStatus() {
    return status;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onMachineStatusChanged(this);
  }

  public interface Handler extends EventHandler {
    void onMachineStatusChanged(MachineStatusChangedEvent event);
  }
}
