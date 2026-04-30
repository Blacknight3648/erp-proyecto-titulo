import { useMemo } from 'react';
import { calculateOPStatus } from '../utils/statusUtils';
import { mockOTs, mockAllOCs, mockOpPersonalizacion } from '../data/mockData';

export const useEstadoOP = (op) => {
    return useMemo(() => calculateOPStatus(op, mockOTs, mockAllOCs, mockOpPersonalizacion), [op]);
};
