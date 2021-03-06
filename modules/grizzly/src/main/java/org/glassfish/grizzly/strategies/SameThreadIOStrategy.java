/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.grizzly.strategies;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.EventLifeCycleListener;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.IOEvent;
import org.glassfish.grizzly.Transport;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

/**
 * {@link org.glassfish.grizzly.IOStrategy}, which executes {@link org.glassfish.grizzly.Processor}s in a current thread.
 *
 *
 */
public final class SameThreadIOStrategy extends AbstractIOStrategy {

    private static final SameThreadIOStrategy INSTANCE = new SameThreadIOStrategy();

    private static final Logger logger = Grizzly.logger(SameThreadIOStrategy.class);


//    private static final InterestProcessingHandlerWhenIoEnabled PROCESSING_HANDLER_WHEN_IO_ENABLED =
//            new InterestProcessingHandlerWhenIoEnabled();
//
//    private static final InterestProcessingHandlerWhenIoDisabled PROCESSING_HANDLER_WHEN_IO_DISABLED =
//            new InterestProcessingHandlerWhenIoDisabled();
    
    // ------------------------------------------------------------ Constructors


    private SameThreadIOStrategy() { }


    // ---------------------------------------------------------- Public Methods


    public static SameThreadIOStrategy getInstance() {
        return INSTANCE;
    }


    // ------------------------------------------------- Methods from IOStrategy

    @Override
    public boolean executeIOEvent(final Connection connection,
            final IOEvent ioEvent, final DecisionListener listener)
            throws IOException {
        
        EventLifeCycleListener lifeCycleListener = null;
        
        if (listener != null) {
            lifeCycleListener = listener.goSync(connection, ioEvent);
        }
        
        fireEvent(connection, ioEvent, lifeCycleListener, logger);
        return true;
    }

    @Override
    public boolean executeIOEvent(final Connection connection,
            final IOEvent ioEvent,
            final EventLifeCycleListener lifeCycleListener) {
        
        fireEvent(connection, ioEvent, lifeCycleListener, logger);
        return true;
    }

    @Override
    public Executor getThreadPoolFor(final Connection connection,
            final IOEvent ioEvent) {
        return null;
    }

    // ----------------------------------- Methods from WorkerThreadPoolConfigProducer


    @Override
    public ThreadPoolConfig createDefaultWorkerPoolConfig(final Transport transport) {
        return null;
    }
}
