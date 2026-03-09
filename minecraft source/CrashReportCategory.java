/*     */ package net.minecraft;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class CrashReportCategory {
/*     */   private final String title;
/*     */   
/*     */   public CrashReportCategory(String title) {
/*  15 */     this.entries = Lists.newArrayList();
/*  16 */     this.stackTrace = new StackTraceElement[0];
/*     */ 
/*     */     
/*  19 */     this.title = title;
/*     */   }
/*     */   private final List<Entry> entries; private StackTraceElement[] stackTrace;
/*     */   
/*  23 */   public static String formatLocation(double x, double y, double z) { return String.format(Locale.ROOT, "%.2f,%.2f,%.2f", new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) }); }
/*     */ 
/*     */ 
/*     */   
/*  27 */   public static String formatLocation(LevelHeightAccessor levelHeightAccessor, double x, double y, double z) { return String.format(Locale.ROOT, "%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), formatLocation(levelHeightAccessor, BlockPos.containing(x, y, z)) }); }
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static String formatLocation(LevelHeightAccessor levelHeightAccessor, BlockPos pos) { return formatLocation(levelHeightAccessor, pos.getX(), pos.getY(), pos.getZ()); }
/*     */ 
/*     */   
/*     */   public static String formatLocation(LevelHeightAccessor levelHeightAccessor, int x, int y, int z) {
/*  35 */     StringBuilder result = new StringBuilder();
/*     */     
/*     */     try {
/*  38 */       result.append(String.format(Locale.ROOT, "World: (%d,%d,%d)", new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z) }));
/*  39 */     } catch (Throwable ignored) {
/*  40 */       result.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  43 */     result.append(", ");
/*     */     
/*     */     try {
/*  46 */       int sectionX = SectionPos.blockToSectionCoord(x);
/*  47 */       int sectionY = SectionPos.blockToSectionCoord(y);
/*  48 */       int sectionZ = SectionPos.blockToSectionCoord(z);
/*  49 */       int relativeX = x & 0xF;
/*  50 */       int relativeY = y & 0xF;
/*  51 */       int relativeZ = z & 0xF;
/*  52 */       int minBlockX = SectionPos.sectionToBlockCoord(sectionX);
/*  53 */       int minBlockY = levelHeightAccessor.getMinY();
/*  54 */       int minBlockZ = SectionPos.sectionToBlockCoord(sectionZ);
/*  55 */       int maxBlockX = SectionPos.sectionToBlockCoord(sectionX + 1) - 1;
/*  56 */       int maxBlockY = levelHeightAccessor.getMaxY();
/*  57 */       int maxBlockZ = SectionPos.sectionToBlockCoord(sectionZ + 1) - 1;
/*  58 */       result.append(String.format(Locale.ROOT, "Section: (at %d,%d,%d in %d,%d,%d; chunk contains blocks %d,%d,%d to %d,%d,%d)", new Object[] { Integer.valueOf(relativeX), Integer.valueOf(relativeY), Integer.valueOf(relativeZ), Integer.valueOf(sectionX), Integer.valueOf(sectionY), Integer.valueOf(sectionZ), Integer.valueOf(minBlockX), Integer.valueOf(minBlockY), Integer.valueOf(minBlockZ), Integer.valueOf(maxBlockX), Integer.valueOf(maxBlockY), Integer.valueOf(maxBlockZ) }));
/*  59 */     } catch (Throwable ignored) {
/*  60 */       result.append("(Error finding chunk loc)");
/*     */     } 
/*     */     
/*  63 */     result.append(", ");
/*     */     
/*     */     try {
/*  66 */       int regionX = x >> 9;
/*  67 */       int regionZ = z >> 9;
/*  68 */       int minChunkX = regionX << 5;
/*  69 */       int minChunkZ = regionZ << 5;
/*  70 */       int maxChunkX = (regionX + 1 << 5) - 1;
/*  71 */       int maxChunkZ = (regionZ + 1 << 5) - 1;
/*  72 */       int minBlockX = regionX << 9;
/*  73 */       int minBlockY = levelHeightAccessor.getMinY();
/*  74 */       int minBlockZ = regionZ << 9;
/*  75 */       int maxBlockX = (regionX + 1 << 9) - 1;
/*  76 */       int maxBlockY = levelHeightAccessor.getMaxY();
/*  77 */       int maxBlockZ = (regionZ + 1 << 9) - 1;
/*  78 */       result.append(String.format(Locale.ROOT, "Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,%d,%d to %d,%d,%d)", new Object[] { Integer.valueOf(regionX), Integer.valueOf(regionZ), Integer.valueOf(minChunkX), Integer.valueOf(minChunkZ), Integer.valueOf(maxChunkX), Integer.valueOf(maxChunkZ), Integer.valueOf(minBlockX), Integer.valueOf(minBlockY), Integer.valueOf(minBlockZ), Integer.valueOf(maxBlockX), Integer.valueOf(maxBlockY), Integer.valueOf(maxBlockZ) }));
/*  79 */     } catch (Throwable ignored) {
/*  80 */       result.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  83 */     return result.toString();
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String key, CrashReportDetail<String> callback) {
/*     */     try {
/*  88 */       setDetail(key, callback.call());
/*  89 */     } catch (Throwable t) {
/*  90 */       setDetailError(key, t);
/*     */     } 
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String key, Object value) {
/*  96 */     this.entries.add(new Entry(key, value));
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 101 */   public void setDetailError(String key, Throwable t) { setDetail(key, t); }
/*     */ 
/*     */   
/*     */   public int fillInStackTrace(int nestedOffset) {
/* 105 */     StackTraceElement[] full = Thread.currentThread().getStackTrace();
/*     */ 
/*     */     
/* 108 */     if (full.length <= 0) {
/* 109 */       return 0;
/*     */     }
/*     */     
/* 112 */     this.stackTrace = new StackTraceElement[full.length - 3 - nestedOffset];
/* 113 */     System.arraycopy(full, 3 + nestedOffset, this.stackTrace, 0, this.stackTrace.length);
/* 114 */     return this.stackTrace.length;
/*     */   }
/*     */   
/*     */   public boolean validateStackTrace(StackTraceElement source, StackTraceElement next) {
/* 118 */     if (this.stackTrace.length == 0 || source == null) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     StackTraceElement current = this.stackTrace[0];
/*     */ 
/*     */     
/* 125 */     if (current.isNativeMethod() != source.isNativeMethod() || 
/* 126 */       !current.getClassName().equals(source.getClassName()) || 
/* 127 */       !current.getFileName().equals(source.getFileName()) || 
/* 128 */       !current.getMethodName().equals(source.getMethodName()))
/*     */     {
/* 130 */       return false;
/*     */     }
/*     */     
/* 133 */     if (((next != null) ? 1 : 0) != ((this.stackTrace.length > 1) ? 1 : 0)) {
/* 134 */       return false;
/*     */     }
/* 136 */     if (next != null && !this.stackTrace[1].equals(next)) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     this.stackTrace[0] = source;
/*     */     
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   public void trimStacktrace(int length) {
/* 146 */     StackTraceElement[] swap = new StackTraceElement[this.stackTrace.length - length];
/* 147 */     System.arraycopy(this.stackTrace, 0, swap, 0, swap.length);
/* 148 */     this.stackTrace = swap;
/*     */   }
/*     */   
/*     */   public void getDetails(StringBuilder builder) {
/* 152 */     builder.append("-- ").append(this.title).append(" --\n");
/* 153 */     builder.append("Details:");
/*     */     
/* 155 */     for (Entry entry : this.entries) {
/* 156 */       builder.append("\n\t");
/* 157 */       builder.append(entry.getKey());
/* 158 */       builder.append(": ");
/* 159 */       builder.append(entry.getValue());
/*     */     } 
/*     */     
/* 162 */     if (this.stackTrace != null && this.stackTrace.length > 0) {
/* 163 */       builder.append("\nStacktrace:");
/*     */       
/* 165 */       for (StackTraceElement element : this.stackTrace) {
/* 166 */         builder.append("\n\tat ");
/* 167 */         builder.append(element);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 173 */   public StackTraceElement[] getStacktrace() { return this.stackTrace; }
/*     */ 
/*     */   
/*     */   public static void populateBlockDetails(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor, BlockPos pos, BlockState state) {
/* 177 */     Objects.requireNonNull(state); category.setDetail("Block", state::toString);
/*     */     
/* 179 */     populateBlockLocationDetails(category, levelHeightAccessor, pos);
/*     */   }
/*     */ 
/*     */   
/* 183 */   public static CrashReportCategory populateBlockLocationDetails(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor, BlockPos pos) { return category.setDetail("Block location", () -> formatLocation(levelHeightAccessor, pos)); }
/*     */   
/*     */   private static class Entry
/*     */   {
/*     */     private final String key;
/*     */     private final String value;
/*     */     
/*     */     public Entry(String key, Object value) {
/* 191 */       this.key = key;
/*     */       
/* 193 */       if (value == null)
/* 194 */       { this.value = "~~NULL~~"; }
/* 195 */       else if (value instanceof Throwable) { Throwable t = (Throwable)value;
/* 196 */         this.value = "~~ERROR~~ " + t.getClass().getSimpleName() + ": " + t.getMessage(); }
/*     */       else
/* 198 */       { this.value = value.toString(); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/* 203 */     public String getKey() { return this.key; }
/*     */ 
/*     */ 
/*     */     
/* 207 */     public String getValue() { return this.value; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\CrashReportCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */