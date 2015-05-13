/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.hadoop.hdfs.web.resources;

import java.net.HttpURLConnection;

/**
 * Extended Webhdfs GetOpParam to avoid redirect operation for azure data
 * lake storage.
 */
public class ADLGetOpParam extends HttpOpParam<ADLGetOpParam.Op> {
  private static final Domain<Op> DOMAIN = new Domain<Op>(NAME, Op.class);

  /**
   * Constructor.
   *
   * @param str a string representation of the parameter value.
   */
  public ADLGetOpParam(final String str) {
    super(DOMAIN, DOMAIN.parse(str));
  }

  @Override
  public final String getName() {
    return NAME;
  }

  /**
   * Get operations.
   */
  public static enum Op implements HttpOpParam.Op {
    OPEN(false, HttpURLConnection.HTTP_OK);

    private final boolean redirect;
    private final int expectedHttpResponseCode;
    private final boolean requireAuth;

    Op(final boolean doRedirect, final int expectHttpResponseCode) {
      this(doRedirect, expectHttpResponseCode, false);
    }

    Op(final boolean doRedirect, final int expectHttpResponseCode,
        final boolean doRequireAuth) {
      this.redirect = doRedirect;
      this.expectedHttpResponseCode = expectHttpResponseCode;
      this.requireAuth = doRequireAuth;
    }

    @Override
    public HttpOpParam.Type getType() {
      return HttpOpParam.Type.GET;
    }

    @Override
    public boolean getRequireAuth() {
      return requireAuth;
    }

    @Override
    public boolean getDoOutput() {
      return false;
    }

    @Override
    public boolean getRedirect() {
      return redirect;
    }

    @Override
    public int getExpectedHttpResponseCode() {
      return expectedHttpResponseCode;
    }

    @Override
    public String toQueryString() {
      return NAME + "=" + this;
    }
  }
}