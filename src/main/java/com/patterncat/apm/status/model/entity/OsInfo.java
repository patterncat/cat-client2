package com.patterncat.apm.status.model.entity;


import com.patterncat.apm.status.model.BaseEntity;
import com.patterncat.apm.status.model.IVisitor;

public class OsInfo extends BaseEntity<OsInfo> {
   private String m_name;

   private String m_arch;

   private String m_version;

   private int m_availableProcessors;

   private double m_systemLoadAverage;

   private long m_processTime;

   private long m_totalPhysicalMemory;

   private long m_freePhysicalMemory;

   private long m_committedVirtualMemory;

   private long m_totalSwapSpace;

   private long m_freeSwapSpace;

   public OsInfo() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitOs(this);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof OsInfo) {
         OsInfo _o = (OsInfo) obj;

         if (!equals(m_name, _o.getName())) {
            return false;
         }

         if (!equals(m_arch, _o.getArch())) {
            return false;
         }

         if (!equals(m_version, _o.getVersion())) {
            return false;
         }

         if (m_availableProcessors != _o.getAvailableProcessors()) {
            return false;
         }

         if (m_systemLoadAverage != _o.getSystemLoadAverage()) {
            return false;
         }

         if (m_processTime != _o.getProcessTime()) {
            return false;
         }

         if (m_totalPhysicalMemory != _o.getTotalPhysicalMemory()) {
            return false;
         }

         if (m_freePhysicalMemory != _o.getFreePhysicalMemory()) {
            return false;
         }

         if (m_committedVirtualMemory != _o.getCommittedVirtualMemory()) {
            return false;
         }

         if (m_totalSwapSpace != _o.getTotalSwapSpace()) {
            return false;
         }

         if (m_freeSwapSpace != _o.getFreeSwapSpace()) {
            return false;
         }


         return true;
      }

      return false;
   }

   public String getArch() {
      return m_arch;
   }

   public int getAvailableProcessors() {
      return m_availableProcessors;
   }

   public long getCommittedVirtualMemory() {
      return m_committedVirtualMemory;
   }

   public long getFreePhysicalMemory() {
      return m_freePhysicalMemory;
   }

   public long getFreeSwapSpace() {
      return m_freeSwapSpace;
   }

   public String getName() {
      return m_name;
   }

   public long getProcessTime() {
      return m_processTime;
   }

   public double getSystemLoadAverage() {
      return m_systemLoadAverage;
   }

   public long getTotalPhysicalMemory() {
      return m_totalPhysicalMemory;
   }

   public long getTotalSwapSpace() {
      return m_totalSwapSpace;
   }

   public String getVersion() {
      return m_version;
   }

   @Override
   public int hashCode() {
      int hash = 0;

      hash = hash * 31 + (m_name == null ? 0 : m_name.hashCode());
      hash = hash * 31 + (m_arch == null ? 0 : m_arch.hashCode());
      hash = hash * 31 + (m_version == null ? 0 : m_version.hashCode());
      hash = hash * 31 + m_availableProcessors;
      hash = hash * 31 + (int) (Double.doubleToLongBits(m_systemLoadAverage) ^ (Double.doubleToLongBits(m_systemLoadAverage) >>> 32));
      hash = hash * 31 + (int) (m_processTime ^ (m_processTime >>> 32));
      hash = hash * 31 + (int) (m_totalPhysicalMemory ^ (m_totalPhysicalMemory >>> 32));
      hash = hash * 31 + (int) (m_freePhysicalMemory ^ (m_freePhysicalMemory >>> 32));
      hash = hash * 31 + (int) (m_committedVirtualMemory ^ (m_committedVirtualMemory >>> 32));
      hash = hash * 31 + (int) (m_totalSwapSpace ^ (m_totalSwapSpace >>> 32));
      hash = hash * 31 + (int) (m_freeSwapSpace ^ (m_freeSwapSpace >>> 32));

      return hash;
   }

   @Override
   public void mergeAttributes(OsInfo other) {
      if (other.getName() != null) {
         m_name = other.getName();
      }

      if (other.getArch() != null) {
         m_arch = other.getArch();
      }

      if (other.getVersion() != null) {
         m_version = other.getVersion();
      }

      m_availableProcessors = other.getAvailableProcessors();

      m_systemLoadAverage = other.getSystemLoadAverage();

      m_processTime = other.getProcessTime();

      m_totalPhysicalMemory = other.getTotalPhysicalMemory();

      m_freePhysicalMemory = other.getFreePhysicalMemory();

      m_committedVirtualMemory = other.getCommittedVirtualMemory();

      m_totalSwapSpace = other.getTotalSwapSpace();

      m_freeSwapSpace = other.getFreeSwapSpace();
   }

   public OsInfo setArch(String arch) {
      m_arch = arch;
      return this;
   }

   public OsInfo setAvailableProcessors(int availableProcessors) {
      m_availableProcessors = availableProcessors;
      return this;
   }

   public OsInfo setCommittedVirtualMemory(long committedVirtualMemory) {
      m_committedVirtualMemory = committedVirtualMemory;
      return this;
   }

   public OsInfo setFreePhysicalMemory(long freePhysicalMemory) {
      m_freePhysicalMemory = freePhysicalMemory;
      return this;
   }

   public OsInfo setFreeSwapSpace(long freeSwapSpace) {
      m_freeSwapSpace = freeSwapSpace;
      return this;
   }

   public OsInfo setName(String name) {
      m_name = name;
      return this;
   }

   public OsInfo setProcessTime(long processTime) {
      m_processTime = processTime;
      return this;
   }

   public OsInfo setSystemLoadAverage(double systemLoadAverage) {
      m_systemLoadAverage = systemLoadAverage;
      return this;
   }

   public OsInfo setTotalPhysicalMemory(long totalPhysicalMemory) {
      m_totalPhysicalMemory = totalPhysicalMemory;
      return this;
   }

   public OsInfo setTotalSwapSpace(long totalSwapSpace) {
      m_totalSwapSpace = totalSwapSpace;
      return this;
   }

   public OsInfo setVersion(String version) {
      m_version = version;
      return this;
   }

}
