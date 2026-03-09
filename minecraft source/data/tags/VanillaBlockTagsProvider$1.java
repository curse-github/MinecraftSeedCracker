/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
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
/*    */   extends BlockItemTagsProvider
/*    */ {
/* 25 */   protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) { return VanillaBlockTagsProvider.this.tag(blockTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\VanillaBlockTagsProvider$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */