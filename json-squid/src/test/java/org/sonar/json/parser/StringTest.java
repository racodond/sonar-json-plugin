/*
 * SonarQube JSON Plugin
 * Copyright (C) 2015 David RACODON
 * david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.json.parser;

import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class StringTest extends TestBase {

  private LexerlessGrammar b = JSONGrammar.createGrammar();

  @Test
  public void should_match_STRING() {
    assertThat(b.rule(JSONGrammar.STRING))
      .matches("\"\"")
      .matches("\"abc\"")
      .matches("\"\\\\\"")
      .matches("\"aa\\\\aa\"")
      .matches("\"aa\\\"aa\"")
      .matches("\"aa\\\"a\na\"")
      .matches("\"aa\\\"a\na\"")
      .matches("\"\\\\\"")
      .matches("\"\\r\"")
      .matches("\"\\u1A3F\"")
      .matches("\"/\"")
      .matches("\"\\/\"")
      .matches("\"## LOL, WUT?\\nIt basically allows you to allow or disallow Flash Player sockets from accessing your site.\\n\\n## Installation\\n\\n```bash\\nnpm install policyfile\\n```\\n## Usage\\n\\nThe server is based on the regular and know `net` and `http` server patterns. So it you can just listen\\nfor all the events that a `net` based server emits etc. But there is one extra event, the `connect_failed`\\nevent. This event is triggered when we are unable to listen on the supplied port number.\\n\\n### createServer\\nCreates a new server instance and accepts 2 optional arguments:\\n\\n-  `options` **Object** Options to configure the server instance\\n    -  `log` **Boolean** Enable logging to STDOUT and STDERR (defaults to true)\\n-  `origins` **Array** An Array of origins that are allowed by the server (defaults to *:*)\\n\\n```js\\nvar pf = require('policyfile');\\npf.createServer();\\npf.listen();\\n```\\n\\n#### server.listen\\nStart listening on the server and it takes 3 optional arguments\\n\\n-  `port` **Number** On which port number should we listen? (defaults to 843, which is the first port number the FlashPlayer checks)\\n-  `server` **Server** A http server, if we are unable to accept requests or run the server we can also answer the policy requests inline over the supplied HTTP server.\\n-  `callback` **Function** A callback function that is called when listening to the server was successful.\\n\\n```js\\nvar pf = require('policyfile');\\npf.createServer();\\npf.listen(1337, function(){\\n  console.log(':3 yay')\\n});\\n```\\n\\nChanging port numbers can be handy if you do not want to run your server as root and have port 843 forward to a non root port number (aka a number above 1024).\\n\\n```js\\nvar pf = require('policyfile')\\n  , http = require('http');\\n\\nserver = http.createServer(function(q,r){r.writeHead(200);r.end('hello world')});\\nserver.listen(80);\\n\\npf.createServer();\\npf.listen(1337, server, function(){\\n  console.log(':3 yay')\\n});\\n```\\n\\nSupport for serving inline requests over a existing HTTP connection as the FlashPlayer will first check port 843, but if it's unable to get a response there it will send a policy file request over port 80, which is usually your http server.\\n\\n#### server.add\\nAdds more origins to the policy file you can add as many arguments as you like.\\n\\n```js\\nvar pf = require('policyfile');\\npf.createServer(['google.com:80']);\\npf.listen();\\npf.add('blog.3rd-Eden.com:80', 'blog.3rd-Eden.com:8080'); // now has 3 origins\\n```\\n\\n#### server.add\\nAdds more origins to the policy file you can add as many arguments as you like.\\n\\n```js\\nvar pf = require('policyfile');\\npf.createServer(['blog.3rd-Eden.com:80', 'blog.3rd-Eden.com:8080']);\\npf.listen();\\npf.remove('blog.3rd-Eden.com:8080'); // only contains the :80 version now\\n```\\n\\n#### server.close\\nShuts down the server\\n\\n```js\\nvar pf = require('policyfile');\\npf.createServer();\\npf.listen();\\npf.close(); // OH NVM.\\n```\\n\\n## API\\nhttp://3rd-eden.com/FlashPolicyFileServer/\\n\\n## Examples\\nSee https://github.com/3rd-Eden/FlashPolicyFileServer/tree/master/examples for examples\\n\\n## Licence\\n\\nMIT see LICENSE file in the repository\"");
  }

  @Test
  public void should_not_match_STRING() {
    assertThat(b.rule(JSONGrammar.STRING))
      .notMatches("123")
      .notMatches("12\\3")
      .notMatches("12\"3")
      .notMatches("\"12\"3\"")
      .notMatches("\"12\\3\"")
      .notMatches("\"\\\"")
      .notMatches("\"\\u13F\"");
  }

}
