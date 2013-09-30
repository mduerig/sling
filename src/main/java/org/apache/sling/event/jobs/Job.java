/*
 1   * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.event.jobs;

import java.util.Calendar;
import java.util.Set;

import org.apache.sling.event.jobs.consumer.JobConsumer;

import aQute.bnd.annotation.ProviderType;

/**
 * A job
 *
 *
 * Property Types
 *
 * In general all scalar types and all serializable classes are supported as
 * property types. However, in order for deseralizing classes these must be
 * exported. Serializable classes are not searchable in the query either.
 * Due to the above mentioned potential problems, it is advisable to not use
 * custom classes as job properties, but rather use out of the box supported
 * types in combination with collections.
 *
 * A resource provider might convert numbers to a different type, JCR is well-known
 * for this behavior as it only supports long but neither integer nor short.
 * Therefore if you are dealing with numbers, use the {@link #getProperty(String, Class)}
 * method to get the correct type instead of directly casting it.
 *
 * @since 1.2
 */
@ProviderType
public interface Job {

    /**
     * The name of the job queue processing this job.
     * This property is set by the job handling when the job is processed.
     * If this property is set by the client creating the job it's value is ignored
     */
    String PROPERTY_JOB_QUEUE_NAME = "event.job.queuename";

    /**
     * This property is set by the job handling to define the priority of this job
     * execution.
     * The property is evaluated by the job handling before starting the
     * {@link JobConsumer} and sets the priority of the thread accordingly.
     * For possible values see {@link JobUtil.JobPriority}.
     * If this property is set by the client creating the job it's value is ignored
     */
    String PROPERTY_JOB_PRIORITY = "event.job.priority";

    /**
     * The property to track the retry count for jobs. Value is of type Integer.
     * On first execution the value of this property is zero.
     * This property is managed by the job handling.
     * If this property is set by the client creating the job it's value is ignored
     */
    String PROPERTY_JOB_RETRY_COUNT = "event.job.retrycount";

    /**
     * The property to track the retry maximum retry count for jobs. Value is of type Integer.
     * This property is managed by the job handling.
     * If this property is set by the client creating the job it's value is ignored
     */
    String PROPERTY_JOB_RETRIES = "event.job.retries";

    /**
     * This property is set by the job handling and contains a calendar object
     * specifying the date and time when this job has been created.
     * If this property is set by the client creating the job it's value is ignored
     */
    String PROPERTY_JOB_CREATED = "slingevent:created";

    /**
     * This property is set by the job handling and contains the Sling instance ID
     * of the instance where this job has been created.
     */
    String PROPERTY_JOB_CREATED_INSTANCE = "slingevent:application";

    /**
     * This property is set by the job handling and contains the Sling instance ID
     * of the instance where this job should be processed.
     */
    String PROPERTY_JOB_TARGET_INSTANCE = "event.job.application";

    /**
     * This property is set by the job handling and contains a calendar object
     * specifying the date and time when this job has been started.
     * This property is only set if the job is currently in processing
     * If this property is set by the client creating the job it's value is ignored
     */
    String PROPERTY_JOB_STARTED_TIME = "event.job.started.time";

    /**
     * The property to set a retry delay. Value is of type Long and specifies milliseconds.
     * This property can be used to override the retry delay from the queue configuration.
     * But it should only be used very rarely as the queue configuration should be the one
     * in charge.
     */
    String PROPERTY_JOB_RETRY_DELAY = "event.job.retrydelay";

    /**
     * This property contains the optional output log of a job consumer.
     * The value of this property is a string array.
     * This property is read-only and can't be specified when the job is created.
     * @since 1.3
     */
    String PROPERTY_JOB_PROGRESS_LOG = "slingevent:progressLog";

    /**
     * This property contains the optional ETA for a job.
     * The value of this property is a {@link Calendar} object.
     * This property is read-only and can't be specified when the job is created.
     * @since 1.3
     */
    String PROPERTY_JOB_PROGRESS_ETA = "slingevent:progressETA";

    /**
     * This property contains optional progress information about a job,
     * the number of steps the job consumer will perform. Each step is
     * assumed to consume roughly the same amount if time.
     * The value of this property is an integer.
     * This property is read-only and can't be specified when the job is created.
     * @since 1.3
     */
    String PROPERTY_JOB_PROGRESS_STEPS = "slingevent:progressSteps";

    /**
     * This property contains optional progress information about a job,
     * the number of completed steps.
     * The value of this property is an integer.
     * This property is read-only and can't be specified when the job is created.
     * @since 1.3
     */
    String PROPERTY_JOB_PROGRESS_STEP = "slingevent:progressStep";

    /**
     * This property contains the optional result message of a job consumer.
     * The value of this property is a string.
     * This property is read-only and can't be specified when the job is created.
     * @since 1.3
     */
    String PROPERTY_RESULT_MESSAGE = "slingevent:resultMessage";

    /**
     * This property contains the finished date once a job is marked as finished.
     * The value of this property is a {@link Calendar} object.
     * This property is read-only and can't be specified when the job is created.
     * @since 1.3
     */
    String PROPERTY_FINISHED_DATE = "slingevent:finishedDate";

    enum JobType {
        QUEUED,
        ACTIVE,
        SUCCEEDED,
        CANCELLED
    };

    /**
     * The job topic.
     * @return The job topic
     */
    String getTopic();

    /**
     * Optional job name
     * @return The job name or <code>null</code>
     */
    String getName();

    /**
     * Unique job ID.
     * @return The unique job ID.
     */
    String getId();

    /**
     * Get the value of a property.
     * @param name The property name
     * @return The value of the property or <code>null</code>
     */
    Object getProperty(String name);

    /**
     * Get all property names.
     * @return A set of property names.
     */
    Set<String> getPropertyNames();

    /**
     * Get a named property and convert it into the given type.
     * This method does not support conversion into a primitive type or an
     * array of a primitive type. It should return <code>null</code> in this
     * case.
     *
     * @param name The name of the property
     * @param type The class of the type
     * @return Return named value converted to type T or <code>null</code> if
     *         non existing or can't be converted.
     */
    <T> T getProperty(String name, Class<T> type);

    /**
     * Get a named property and convert it into the given type.
     * This method does not support conversion into a primitive type or an
     * array of a primitive type. It should return the default value in this
     * case.
     *
     * @param name The name of the property
     * @param defaultValue The default value to use if the named property does
     *            not exist or cannot be converted to the requested type. The
     *            default value is also used to define the type to convert the
     *            value to. If this is <code>null</code> any existing property is
     *            not converted.
     * @return Return named value converted to type T or the default value if
     *         non existing or can't be converted.
     */
    <T> T getProperty(String name, T defaultValue);

    /**
     * This property is set by the job handling to define the priority of this job
     * execution.
     * The property is evaluated by the job handling before starting the
     * {@link JobConsumer} and sets the priority of the thread accordingly.
     * For possible values see {@link JobUtil.JobPriority}.
     */
    JobUtil.JobPriority getJobPriority();

    /**
     * On first execution the value of this property is zero.
     * This property is managed by the job handling.
     */
    int getRetryCount();

    /**
     * The property to track the retry maximum retry count for jobs.
     * This property is managed by the job handling.
     */
    int getNumberOfRetries();

    /**
     * The name of the job queue processing this job.
     * This property is set by the job handling when the job is processed.
     * @return The queue name or <code>null</code>
     */
    String getQueueName();

    /**
     * This property is set by the job handling and contains the Sling instance ID
     * of the instance where this job should be processed.
     * @return The sling ID or <code>null</code>
     */
    String getTargetInstance();

    /**
     * This property is set by the job handling and contains a calendar object
     * specifying the date and time when this job has been started.
     * This property is only set if the job is currently in processing
     */
    Calendar getProcessingStarted();

    /**
     * This property is set by the job handling and contains a calendar object
     * specifying the date and time when this job has been created.
     */
    Calendar getCreated();

    /**
     * This property is set by the job handling and contains the Sling instance ID
     * of the instance where this job has been created.
     */
    String getCreatedInstance();

    /**
     * Get the job type
     * @since 1.3
     */
    JobType getJobType();

    /**
     * If the job is cancelled or succeeded, this method will return the finish date.
     * @return The finish date or <code>null</code>
     * @since 1.3
     */
    Calendar getFinishedDate();

    /**
     * This method returns the message from the last job processing, regardless
     * whether the processing failed, succeeded or was cancelled. The message
     * is optional and can be set by a job consumer.
     * @return The result message or <code>null</code>
     * @since 1.3
     */
    String getResultMessage();

    /**
     * TODO
     * @since 1.3
     */
    String[] getProgressLog();

    /**
     * TODO
     * @since 1.3
     */
    int getProgressStepCount();

    /**
     * TODO
     * @since 1.3
     */
    int getCurrentProgressStep();

    /**
     * TODO
     * @since 1.3
     */
    Calendar getProgressETA();
}