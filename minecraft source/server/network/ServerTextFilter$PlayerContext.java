/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
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
/*     */ public class PlayerContext
/*     */   implements TextFilter
/*     */ {
/*     */   protected final GameProfile profile;
/*     */   protected final Executor streamExecutor;
/*     */   
/*     */   protected PlayerContext(GameProfile profile) {
/* 209 */     this.profile = profile;
/* 210 */     ConsecutiveExecutor streamProcessor = new ConsecutiveExecutor(ServerTextFilter.this.workerPool, "chat stream for " + profile.name());
/* 211 */     Objects.requireNonNull(streamProcessor); this.streamExecutor = streamProcessor::schedule;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> messages) {
/* 219 */     List<CompletableFuture<FilteredText>> requests = (List)messages.stream().map(message -> ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor)).collect(ImmutableList.toImmutableList());
/*     */     
/* 221 */     return Util.sequenceFailFast(requests)
/*     */       
/* 223 */       .exceptionally(e -> ImmutableList.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   public CompletableFuture<FilteredText> processStreamMessage(String message) { return ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerTextFilter$PlayerContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */