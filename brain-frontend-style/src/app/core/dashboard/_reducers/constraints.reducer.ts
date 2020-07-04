
// NGRX
import { createFeatureSelector } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
// Actions
import { OptModelConstraintsActionTypes,OptModelConstraintsActions } from '../_actions/opt-model-constraints-actions';
// Models
import { OptModelConstraints } from '../_model/opt-model-constraints';

export interface  OptModelConstraintsState extends EntityState<OptModelConstraints> {
    modelLoading: boolean;
    actionsloading: boolean;
    totalCount: number;
    showInitWaitingMessage: boolean;
}

export const adapter: EntityAdapter<OptModelConstraints> = createEntityAdapter<OptModelConstraints>();

export const initialOptModelConstraintsState: OptModelConstraintsState  = adapter.getInitialState({
    modelLoading: false,
    actionsloading: false,
    totalCount: 0,
    showInitWaitingMessage: true
});

export function OptModelConstraintsReducer(state = initialOptModelConstraintsState, action: OptModelConstraintsActions): OptModelConstraintsState {
    switch  (action.type) {    
        case OptModelConstraintsActionTypes.OptModelConstraintsListLoaded:
            return adapter.addMany(action.payload.constraints, {
                ...initialOptModelConstraintsState,
                totalCount: action.payload.totalCount,
                listLoading: false,
                showInitWaitingMessage: false                
            });
        default: return state;
    }
}

export const getConstraintsState = createFeatureSelector<OptModelConstraints>('constraints');

export const {
    selectAll,
    selectEntities,
    selectIds,
    selectTotal
} = adapter.getSelectors();
