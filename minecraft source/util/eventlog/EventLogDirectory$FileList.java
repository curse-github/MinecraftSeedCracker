/*     */ package net.minecraft.util.eventlog;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileList
/*     */   extends Object
/*     */   implements Iterable<EventLogDirectory.File>
/*     */ {
/*     */   private final List<EventLogDirectory.File> files;
/*     */   
/* 130 */   private FileList(List<EventLogDirectory.File> files) { this.files = new ArrayList(files); }
/*     */ 
/*     */   
/*     */   public FileList prune(LocalDate date, int expiryDays) {
/* 134 */     this.files.removeIf(file -> {
/* 135 */           EventLogDirectory.FileId id = file.id();
/* 136 */           LocalDate expiresAt = id.date().plusDays(expiryDays);
/* 137 */           if (!date.isBefore(expiresAt)) {
/*     */             try {
/* 139 */               Files.delete(file.path());
/* 140 */               return true;
/* 141 */             } catch (IOException e) {
/* 142 */               EventLogDirectory.LOGGER.warn("Failed to delete expired event log file: {}", file.path(), e);
/*     */             } 
/*     */           }
/* 145 */           return false;
/*     */         });
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public FileList compressAll() {
/* 151 */     ListIterator<EventLogDirectory.File> iterator = this.files.listIterator();
/* 152 */     while (iterator.hasNext()) {
/* 153 */       EventLogDirectory.File file = (EventLogDirectory.File)iterator.next();
/*     */       try {
/* 155 */         iterator.set(file.compress());
/* 156 */       } catch (IOException e) {
/* 157 */         EventLogDirectory.LOGGER.warn("Failed to compress event log file: {}", file.path(), e);
/*     */       } 
/*     */     } 
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public Iterator<EventLogDirectory.File> iterator() { return this.files.iterator(); }
/*     */ 
/*     */ 
/*     */   
/* 169 */   public Stream<EventLogDirectory.File> stream() { return this.files.stream(); }
/*     */ 
/*     */ 
/*     */   
/* 173 */   public Set<EventLogDirectory.FileId> ids() { return (Set)this.files.stream().map(EventLogDirectory.File::id).collect(Collectors.toSet()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\eventlog\EventLogDirectory$FileList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */