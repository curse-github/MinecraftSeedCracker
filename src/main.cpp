#include <iostream>
#include <vector>
#include "StrongholdStructure.h"
#include "Finder.h"

void desmosPrintPositions(const std::vector<ChunkPos>& positions, const int& radius = 50000) {
    std::cout << "\npaste into the javascript console of https://www.desmos.com/calculator.\n\n";
    std::cout << "document.getElementById('graph-container').innerHTML = \"\";\n";
    std::cout << "var calculator = Desmos.GraphingCalculator(document.getElementById('graph-container'));\n";
    std::cout << "calculator.setMathBounds({left:" << -radius << ",right:" << radius << ",bottom:" << -radius << ",top:" << radius << ", color:'#000000'});\n";
    std::cout << "calculator.updateSettings({xAxisLabel:'X-axis',xAxisMinorSubdivisions:1,xAxisStep:16,yAxisLabel:'Z-axis',yAxisMinorSubdivisions:1,yAxisStep:16,expressionsCollapsed:true});\n";
    for (unsigned int i = 0; i < positions.size(); i++) {
        const Pos pos = positions[i].getOffsetPos({ 4, 0, 4 });
        std::cout << "calculator.setExpression({id:'graph" << (i + 1) << "', latex:'(" << pos.x << "," << -pos.z << ")'});\n";
    }
    std::cout << '\n';
    for (unsigned int i = 0; i < positions.size(); i++) {
        const Pos pos = positions[i].getOffsetPos({ 4, 0, 4 });
        std::cout << "www.chunkbase.com/apps/stronghold-finder#seed=" << worldSeed << "&platform=java_1_21_9&x=" << pos.x << "&z=" << pos.z << "&zoom=1.25\n";
    }
}
void desmosPrintBoundingBoxes(const Pos& entrance, const std::vector<BoundingBox>& boxes, const Pos& portalPos) {
    std::cout << "\npaste into the javascript console of https://www.desmos.com/calculator.\n\n";
    std::cout << "document.getElementById('graph-container').innerHTML = \"\";\n";
    std::cout << "var calculator = Desmos.GraphingCalculator(document.getElementById('graph-container'));\n";
    int radius = 150;
    std::cout << "calculator.setMathBounds({left:" << (portalPos.x - radius) << ",right:" << (portalPos.x + radius) << ",bottom:" << (-portalPos.z - radius) << ",top:" << (-portalPos.z + radius) << ", color:'#000000'});\n";
    std::cout << "calculator.updateSettings({xAxisLabel:'X-axis',xAxisMinorSubdivisions:1,xAxisStep:16,yAxisLabel:'Z-axis',yAxisMinorSubdivisions:1,yAxisStep:16,expressionsCollapsed:true});\n";
    std::cout << "calculator.setExpression({id:'graph1', latex:'(" << entrance.x << "," << -entrance.z << ")',label:'Entrance',showLabel:true,color:'#0000ff',pointSize:'16',pointStyle:'CROSS'});\n";
    for (unsigned int i = 0; i < boxes.size(); i++) {
        const BoundingBox boundingBox = boxes[i];
        std::cout << "calculator.setExpression({id:'graph" << (i + 2) << "', latex:'\\\\operatorname{polygon}(";
        std::cout << "(" << boundingBox.minX << ", " << -boundingBox.minZ << "), ";
        std::cout << "(" << boundingBox.maxX << ", " << -boundingBox.minZ << "), ";
        std::cout << "(" << boundingBox.maxX << ", " << -boundingBox.maxZ << "), ";
        std::cout << "(" << boundingBox.minX << ", " << -boundingBox.maxZ << ")";
        std::cout << ")', color:'" << boundingBox.color << "'});\n";
    }
    std::cout << "calculator.setExpression({id:'graph" << (boxes.size() + 2) << "', latex:'(" << portalPos.x << "," << -portalPos.z << ")',label:'Portal Room',showLabel: true,color:'#ff00ff',pointSize:'16',pointStyle:'CROSS'});\n";
}

const long long int worldSeed = 8606738414634885904ull;
const bool constantPosition = false;
int main(int argc, char** argv) {
    // testRand();
    // solve12Eye();
    if (constantPosition) {
        ChunkPos startChunkPos = Pos(1844, 0, -1643).getChunkPos();// found with www.chunkbase.com/apps/stronghold-finder
        OwningNullable<StartEndBoxes> strongholdDataOwner = getPortalRoomPosition(worldSeed, startChunkPos, true);
        StartEndBoxes& strongholdData = strongholdDataOwner.getValue();
        BoundingBox boundingBox = strongholdData.boxes[0];
        for (size_t i = 1; i < strongholdData.boxes.size(); i++) boundingBox.encompass(strongholdData.boxes[i]);
        strongholdData.boxes.push_back(boundingBox);
        desmosPrintBoundingBoxes(strongholdData.start, strongholdData.boxes, strongholdData.end);
        std::cout << "www.chunkbase.com/apps/stronghold-finder#seed=" << worldSeed << "&platform=java_1_21_9&x=" << strongholdData.start.x << "&z=" << strongholdData.start.z << "&zoom=1.25\n";
        std::cout << "entrance pos: /tp " << strongholdData.start.x << " " << strongholdData.start.y << " " << strongholdData.start.z << "\n";
        std::cout << "portal pos: /tp " << strongholdData.end.x << " " << strongholdData.end.y << " " << strongholdData.end.z << "\n";
    } else {
        std::vector<ChunkPos> strongholdPositions = Finder().generateRingPositions(worldSeed);
        desmosPrintPositions(strongholdPositions);
    }
    return 0;
}