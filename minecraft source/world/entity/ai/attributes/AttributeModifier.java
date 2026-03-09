/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public final class AttributeModifier extends Record {
/*    */   private final Identifier id;
/*    */   private final double amount;
/*    */   private final Operation operation;
/*    */   
/* 15 */   public AttributeModifier(Identifier id, double amount, Operation operation) { this.id = id; this.amount = amount; this.operation = operation; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public double amount() { return this.amount; } public Operation operation() { return this.operation; }
/*    */   
/* 17 */   public enum Operation implements StringRepresentable { ADD_VALUE("add_value", 0),
/* 18 */     ADD_MULTIPLIED_BASE("add_multiplied_base", 1),
/* 19 */     ADD_MULTIPLIED_TOTAL("add_multiplied_total", 2); public static final IntFunction<Operation> BY_ID; public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC; public static final Codec<Operation> CODEC; private final String name; private final int id;
/*    */     static  {
/* 21 */       BY_ID = ByIdMap.continuous(Operation::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */       
/* 23 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Operation::id);
/*    */       
/* 25 */       CODEC = StringRepresentable.fromEnum(Operation::values);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     Operation(String name, int id) {
/* 31 */       this.name = name;
/* 32 */       this.id = id;
/*    */     }
/*    */ 
/*    */     
/* 36 */     public int id() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     public String getSerializedName() { return this.name; } }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static final MapCodec<AttributeModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 46 */         .fieldOf("id").forGetter(AttributeModifier::id), Codec.DOUBLE
/* 47 */         .fieldOf("amount").forGetter(AttributeModifier::amount), Operation.CODEC
/* 48 */         .fieldOf("operation").forGetter(AttributeModifier::operation))
/* 49 */       .apply(i, AttributeModifier::new));
/* 50 */   public static final Codec<AttributeModifier> CODEC = MAP_CODEC.codec();
/*    */   
/* 52 */   public static final StreamCodec<ByteBuf, AttributeModifier> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AttributeModifier::id, ByteBufCodecs.DOUBLE, AttributeModifier::amount, Operation.STREAM_CODEC, AttributeModifier::operation, AttributeModifier::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public boolean is(Identifier id) { return id.equals(this.id); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */