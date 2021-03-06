package com.patterncat.apm.service;


import com.patterncat.apm.message.*;
import com.patterncat.apm.message.internal.*;
import com.patterncat.apm.message.spi.MessageManager;
import com.patterncat.apm.message.spi.MessageTree;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultMessageProducer implements MessageProducer {
    @Autowired
    private MessageManager m_manager;

    @Autowired
    private MessageIdFactory m_factory;

    @Override
    public String createMessageId() {
        return m_factory.getNextId();
    }

    @Override
    public boolean isEnabled() {
        return m_manager.isMessageEnabled();
    }

    @Override
    public void logError(String message, Throwable cause) {
        if (m_manager.isCatEnabled()) {
            if (shouldLog(cause)) {
                m_manager.getThreadLocalMessageTree().setSample(false);

                StringWriter writer = new StringWriter(2048);

                if (message != null) {
                    writer.write(message);
                    writer.write(' ');
                }

                cause.printStackTrace(new PrintWriter(writer));

                String detailMessage = writer.toString();

                if (cause instanceof Error) {
                    logEvent("Error", cause.getClass().getName(), "ERROR", detailMessage);
                } else if (cause instanceof RuntimeException) {
                    logEvent("RuntimeException", cause.getClass().getName(), "ERROR", detailMessage);
                } else {
                    logEvent("Exception", cause.getClass().getName(), "ERROR", detailMessage);
                }
            }
        } else {
            cause.printStackTrace();
        }
    }

    @Override
    public void logError(Throwable cause) {
        logError(null, cause);
    }

    @Override
    public void logError(String message) {
        if (m_manager.isCatEnabled()) {
            logEvent("Error","error", "ERROR", message);
        }
    }

    @Override
    public void logEvent(String type, String name) {
        logEvent(type, name, Message.SUCCESS, null);
    }

    @Override
    public void logEvent(String type, String name, String status, String nameValuePairs) {
        Event event = newEvent(type, name);

        if (nameValuePairs != null && nameValuePairs.length() > 0) {
            event.addData(nameValuePairs);
        }

        event.setStatus(status);
        event.complete();
    }

    @Override
    public void logHeartbeat(String type, String name, String status, String nameValuePairs) {
        Heartbeat heartbeat = newHeartbeat(type, name);

        heartbeat.addData(nameValuePairs);
        heartbeat.setStatus(status);
        heartbeat.complete();
    }

    @Override
    public void logMetric(String name, String status, String nameValuePairs) {
        String type = "";
        Metric metric = newMetric(type, name);

        if (nameValuePairs != null && nameValuePairs.length() > 0) {
            metric.addData(nameValuePairs);
        }

        metric.setStatus(status);
        metric.complete();
    }

    @Override
    public void logTrace(String type, String name) {
        logTrace(type, name, Message.SUCCESS, null);
    }

    @Override
    public void logTrace(String type, String name, String status, String nameValuePairs) {
        if (m_manager.isTraceMode()) {
            Trace trace = newTrace(type, name);

            if (nameValuePairs != null && nameValuePairs.length() > 0) {
                trace.addData(nameValuePairs);
            }

            trace.setStatus(status);
            trace.complete();
        }
    }

    @Override
    public Event newEvent(String type, String name) {
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled()) {
            DefaultEvent event = new DefaultEvent(type, name, m_manager);

            return event;
        } else {
            return NullMessage.EVENT;
        }
    }

    public Event newEvent(Transaction parent, String type, String name) {
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled() && parent != null) {
            DefaultEvent event = new DefaultEvent(type, name);

            parent.addChild(event);
            return event;
        } else {
            return NullMessage.EVENT;
        }
    }

    @Override
    public ForkedTransaction newForkedTransaction(String type, String name) {
        // this enable CAT client logging cat message without explicit setup
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled()) {
            MessageTree tree = m_manager.getThreadLocalMessageTree();

            if (tree.getMessageId() == null) {
                tree.setMessageId(createMessageId());
            }

            DefaultForkedTransaction transaction = new DefaultForkedTransaction(type, name, m_manager);

            if (m_manager instanceof DefaultMessageManager) {
                ((DefaultMessageManager) m_manager).linkAsRunAway(transaction);
            }
            m_manager.start(transaction, true);
            return transaction;
        } else {
            return NullMessage.TRANSACTION;
        }
    }

    @Override
    public Heartbeat newHeartbeat(String type, String name) {
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled()) {
            DefaultHeartbeat heartbeat = new DefaultHeartbeat(type, name, m_manager);

            m_manager.getThreadLocalMessageTree().setSample(false);
            return heartbeat;
        } else {
            return NullMessage.HEARTBEAT;
        }
    }

    @Override
    public Metric newMetric(String type, String name) {
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled()) {
            DefaultMetric metric = new DefaultMetric(type == null ? "" : type, name, m_manager);

            m_manager.getThreadLocalMessageTree().setSample(false);
            return metric;
        } else {
            return NullMessage.METRIC;
        }
    }

    @Override
    public TaggedTransaction newTaggedTransaction(String type, String name, String tag) {
        // this enable CAT client logging cat message without explicit setup
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled()) {
            MessageTree tree = m_manager.getThreadLocalMessageTree();

            if (tree.getMessageId() == null) {
                tree.setMessageId(createMessageId());
            }
            DefaultTaggedTransaction transaction = new DefaultTaggedTransaction(type, name, tag, m_manager);

            m_manager.start(transaction, true);
            return transaction;
        } else {
            return NullMessage.TRANSACTION;
        }
    }

    @Override
    public Trace newTrace(String type, String name) {
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled()) {
            DefaultTrace trace = new DefaultTrace(type, name, m_manager);

            return trace;
        } else {
            return NullMessage.TRACE;
        }
    }

    @Override
    public Transaction newTransaction(String type, String name) {
        // this enable CAT client logging cat message without explicit setup
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled()) {
            DefaultTransaction transaction = new DefaultTransaction(type, name, m_manager);

            m_manager.start(transaction, false);
            return transaction;
        } else {
            return NullMessage.TRANSACTION;
        }
    }

    public Transaction newTransaction(Transaction parent, String type, String name) {
        // this enable CAT client logging cat message without explicit setup
        if (!m_manager.hasContext()) {
            m_manager.setup();
        }

        if (m_manager.isMessageEnabled() && parent != null) {
            DefaultTransaction transaction = new DefaultTransaction(type, name, m_manager);

            parent.addChild(transaction);
            transaction.setStandalone(false);
            return transaction;
        } else {
            return NullMessage.TRANSACTION;
        }
    }

    private boolean shouldLog(Throwable e) {
        if (m_manager instanceof DefaultMessageManager) {
            return ((DefaultMessageManager) m_manager).shouldLog(e);
        } else {
            return true;
        }
    }
}
