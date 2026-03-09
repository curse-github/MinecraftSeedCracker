/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NodeResolver<S>
/*     */   extends Object
/*     */ {
/*     */   private final CommandBuildContext context;
/*     */   private final ClientboundCommandsPacket.NodeBuilder<S> builder;
/*     */   private final List<ClientboundCommandsPacket.Entry> entries;
/*     */   private final List<CommandNode<S>> nodes;
/*     */   
/*     */   private NodeResolver(CommandBuildContext context, ClientboundCommandsPacket.NodeBuilder<S> builder, List<ClientboundCommandsPacket.Entry> entries) {
/* 300 */     this.context = context;
/* 301 */     this.builder = builder;
/* 302 */     this.entries = entries;
/* 303 */     ObjectArrayList<CommandNode<S>> nodes = new ObjectArrayList<CommandNode<S>>();
/* 304 */     nodes.size(entries.size());
/* 305 */     this.nodes = nodes;
/*     */   }
/*     */   
/*     */   public CommandNode<S> resolve(int index) {
/* 309 */     CommandNode<S> result, currentNode = (CommandNode)this.nodes.get(index);
/* 310 */     if (currentNode != null) {
/* 311 */       return currentNode;
/*     */     }
/*     */     
/* 314 */     ClientboundCommandsPacket.Entry entry = (ClientboundCommandsPacket.Entry)this.entries.get(index);
/*     */ 
/*     */     
/* 317 */     if (entry.stub == null) {
/* 318 */       result = new RootCommandNode();
/*     */     } else {
/* 320 */       ArgumentBuilder<S, ?> resultBuilder = entry.stub.build(this.context, this.builder);
/* 321 */       if ((entry.flags & 0x8) != 0) {
/* 322 */         resultBuilder.redirect(resolve(entry.redirect));
/*     */       }
/* 324 */       boolean isExecutable = ((entry.flags & 0x4) != 0);
/* 325 */       boolean isRestricted = ((entry.flags & 0x20) != 0);
/* 326 */       result = this.builder.configure(resultBuilder, isExecutable, isRestricted).build();
/*     */     } 
/* 328 */     this.nodes.set(index, result);
/*     */     
/* 330 */     for (int childId : entry.children) {
/* 331 */       CommandNode<S> child = resolve(childId);
/* 332 */       if (!(child instanceof RootCommandNode)) {
/* 333 */         result.addChild(child);
/*     */       }
/*     */     } 
/* 336 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */