/*
 * Copyright 2013-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.cli.testdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.facebook.buck.cli.TargetDeviceOptions;
import com.facebook.buck.step.TargetDevice;

import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class TargetDeviceOptionsTest {

  @Test
  public void shouldReturnAnAbsentOptionalIfNoTargetDeviceOptionsSet() {
    TargetDeviceOptions options = buildOptions();

    assertFalse(options.getTargetDeviceOptional().isPresent());
  }

  @Test
  public void shouldReturnAnEmulatorIfOnlyEmulatorFlagSet() {
    TargetDeviceOptions options = buildOptions("-e");

    TargetDevice device = options.getTargetDeviceOptional().get();

    assertTrue(device.isEmulator());
    assertNull(device.getIdentifier());
  }

  @Test
  public void shouldReturnADeviceIfOnlyDeviceFlagSet() {
    TargetDeviceOptions options = buildOptions("-d");

    TargetDevice device = options.getTargetDeviceOptional().get();

    assertFalse(device.isEmulator());
    assertNull(device.getIdentifier());
  }

  @Test
  public void onlySettingTheSerialFlagAssumesTheTargetIsARealDevice() {
    TargetDeviceOptions options = buildOptions("-s", "1234");

    TargetDevice device = options.getTargetDeviceOptional().get();

    assertFalse(device.isEmulator());
    assertEquals("1234", device.getIdentifier());
  }

  private TargetDeviceOptions buildOptions(String... args) {
    TargetDeviceOptions options = new TargetDeviceOptions();

    try {
      new CmdLineParser(options).parseArgument(args);
    } catch (CmdLineException e) {
      fail("Unable to parse arguments");
    }

    return options;
  }
}
