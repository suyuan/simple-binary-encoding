/*
 * Copyright 2013 Real Logic Ltd.
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
package uk.co.real_logic.sbe.generation.java.util;

import javax.tools.*;
import java.io.IOException;
import java.security.SecureClassLoader;

public class ClassFileManager<M extends JavaFileManager> extends ForwardingJavaFileManager<M>
{
    private JavaClassObject javaClassObject;

    public ClassFileManager(final M standardManager)
    {
        super(standardManager);
    }

    public ClassLoader getClassLoader(final Location location)
    {
        return new SecureClassLoader()
        {
            protected Class<?> findClass(String name)
                throws ClassNotFoundException
            {
                final byte[] buffer = javaClassObject.getBytes();
                return super.defineClass(name, buffer, 0, buffer.length);
            }
        };
    }

    public JavaFileObject getJavaFileForOutput(final Location location,
                                               final String className,
                                               final JavaFileObject.Kind kind,
                                               final FileObject sibling)
        throws IOException
    {
        javaClassObject = new JavaClassObject(className, kind);

        return javaClassObject;
    }
}
