/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ 
/*    */ public static enum Operation
/*    */   implements StringRepresentable
/*    */ {
/*    */   public static final IntFunction<Operation> BY_ID;
/*    */   public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC;
/* 17 */   ADD_VALUE("add_value", 0),
/* 18 */   ADD_MULTIPLIED_BASE("add_multiplied_base", 1),
/* 19 */   ADD_MULTIPLIED_TOTAL("add_multiplied_total", 2);
/*    */   static  {
/* 21 */     BY_ID = ByIdMap.continuous(Operation::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Operation::id);
/*    */     
/* 25 */     CODEC = StringRepresentable.fromEnum(Operation::values);
/*    */   }
/*    */   
/*    */   public static final Codec<Operation> CODEC;
/*    */   
/*    */   Operation(String name, int id) {
/* 31 */     this.name = name;
/* 32 */     this.id = id;
/*    */   }
/*    */   private final String name; private final int id;
/*    */   
/* 36 */   public int id() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeModifier$Operation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */