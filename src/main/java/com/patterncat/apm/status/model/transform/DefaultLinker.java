package com.patterncat.apm.status.model.transform;

import com.patterncat.apm.status.model.entity.*;

import java.util.ArrayList;
import java.util.List;

public class DefaultLinker implements ILinker {
   private boolean m_deferrable;

   private List<Runnable> m_deferedJobs = new ArrayList<Runnable>();

   public DefaultLinker(boolean deferrable) {
      m_deferrable = deferrable;
   }

   public void finish() {
      for (Runnable job : m_deferedJobs) {
         job.run();
      }
   }

   @Override
   public boolean onDisk(final StatusInfo parent, final DiskInfo disk) {
      parent.setDisk(disk);
      return true;
   }

   @Override
   public boolean onDiskVolume(final DiskInfo parent, final DiskVolumeInfo diskVolume) {
      parent.addDiskVolume(diskVolume);
      return true;
   }

   @Override
   public boolean onExtension(final StatusInfo parent, final Extension extension) {
      if (m_deferrable) {
         m_deferedJobs.add(new Runnable() {
            @Override
            public void run() {
               parent.addExtension(extension);
            }
         });
      } else {
         parent.addExtension(extension);
      }

      return true;
   }

   @Override
   public boolean onExtensionDetail(final Extension parent, final ExtensionDetail extensionDetail) {
      if (m_deferrable) {
         m_deferedJobs.add(new Runnable() {
            @Override
            public void run() {
               parent.addExtensionDetail(extensionDetail);
            }
         });
      } else {
         parent.addExtensionDetail(extensionDetail);
      }

      return true;
   }

   @Override
   public boolean onGc(final MemoryInfo parent, final GcInfo gc) {
      parent.addGc(gc);
      return true;
   }

   @Override
   public boolean onMemory(final StatusInfo parent, final MemoryInfo memory) {
      parent.setMemory(memory);
      return true;
   }

   @Override
   public boolean onMessage(final StatusInfo parent, final MessageInfo message) {
      parent.setMessage(message);
      return true;
   }

   @Override
   public boolean onOs(final StatusInfo parent, final OsInfo os) {
      parent.setOs(os);
      return true;
   }

   @Override
   public boolean onRuntime(final StatusInfo parent, final RuntimeInfo runtime) {
      parent.setRuntime(runtime);
      return true;
   }

   @Override
   public boolean onThread(final StatusInfo parent, final ThreadsInfo thread) {
      parent.setThread(thread);
      return true;
   }
}
