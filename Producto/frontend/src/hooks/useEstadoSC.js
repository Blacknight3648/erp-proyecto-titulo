import { useMemo } from 'react';
import { calculateSCStatus } from '../utils/statusUtils';

export const useEstadoSC = (sc) => {
    return useMemo(() => calculateSCStatus(sc), [sc]);
};
