package util;

import model.Inequality;

public class InequalitiesGenerator {

    public Inequality[] generate(
            double startA
            , double endA
            , double freeStart
            , double freeEnd
            , int count
            , Inequality.Sign sign
            , boolean isZero
    ) {
        if(count == 0) return new Inequality[0];
        if(count == 1) return new Inequality[]{ new Inequality(new double[] {startA, freeStart}, sign, isZero) };

        Inequality[] top = new Inequality[count];
        double topStepA = ( endA - startA ) / (count - 1);
        double topFreeStep = isZero ? ( freeEnd - freeStart ) / (count - 1) : ( freeEnd - freeStart ) / ( (count - 1) / 2.0 );
        for(int i = 0; i < count; i++) {
            double j = isZero || i < count / 2 ? i : count - i - 1;
            top[i] = new Inequality(new double[] { startA + i * topStepA, freeStart + j * topFreeStep}, sign, isZero);
        }
        return top;
    }

}
