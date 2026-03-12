/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.VarInt;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
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
/*    */ class null
/*    */   extends Object
/*    */   implements StreamCodec<ByteBuf, Optional<BlockState>>
/*    */ {
/*    */   public void encode(ByteBuf output, Optional<BlockState> value) {
/* 82 */     if (value.isPresent()) {
/* 83 */       VarInt.write(output, Block.getId((BlockState)value.get()));
/*    */     } else {
/* 85 */       VarInt.write(output, 0);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<BlockState> decode(ByteBuf input) {
/* 91 */     int id = VarInt.read(input);
/* 92 */     if (id == 0) {
/* 93 */       return Optional.empty();
/*    */     }
/* 95 */     return Optional.of(Block.stateById(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\EntityDataSerializers$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */