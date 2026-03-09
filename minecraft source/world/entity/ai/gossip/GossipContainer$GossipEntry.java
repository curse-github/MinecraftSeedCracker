/*    */ package net.minecraft.world.entity.ai.gossip;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class GossipEntry
/*    */   extends Record
/*    */ {
/*    */   private final UUID target;
/*    */   private final GossipType type;
/*    */   private final int value;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 37 */   private GossipEntry(UUID target, GossipType type, int value) { this.target = target; this.type = type; this.value = value; } public UUID target() { return this.target; } public GossipType type() { return this.type; } public int value() { return this.value; }
/* 38 */   public static final Codec<GossipEntry> CODEC = RecordCodecBuilder.create(i -> i.group(UUIDUtil.CODEC
/* 39 */         .fieldOf("Target").forGetter(GossipEntry::target), GossipType.CODEC
/* 40 */         .fieldOf("Type").forGetter(GossipEntry::type), ExtraCodecs.POSITIVE_INT
/* 41 */         .fieldOf("Value").forGetter(GossipEntry::value))
/* 42 */       .apply(i, GossipEntry::new));
/*    */ 
/*    */   
/* 45 */   public int weightedValue() { return this.value * this.type.weight; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\gossip\GossipContainer$GossipEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */